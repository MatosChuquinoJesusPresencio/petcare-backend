package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.UsuarioPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioPort usuarioPort;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioPort usuarioPort, PasswordEncoder passwordEncoder) {
        this.usuarioPort = usuarioPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioPort.findByUsername(usuario.getUsername()).isPresent()) {
            throw new ResourceDuplicateException("El nombre de usuario ya está registrado");
        }
        if (usuarioPort.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResourceDuplicateException("El correo electrónico ya está registrado");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioPort.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioPort.findById(id);
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioPort.findByUsername(username);
    }

    public List<Usuario> listarTodos() {
        return usuarioPort.findAll();
    }
}
