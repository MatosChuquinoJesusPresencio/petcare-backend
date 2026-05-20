package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepositoryPort.findByUsername(usuario.getUsername()).isPresent()) {
            throw new ResourceDuplicateException("Username is already registered");
        }
        if (usuarioRepositoryPort.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResourceDuplicateException("Email address is already registered");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepositoryPort.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepositoryPort.findById(id);
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepositoryPort.findByUsername(username);
    }

    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepositoryPort.findAll(pageable);
    }
}
