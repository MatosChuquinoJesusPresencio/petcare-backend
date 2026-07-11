package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null) {
            throw new BusinessRuleException("Email must not be null");
        }
        if (usuarioRepositoryPort.findByEmail(usuario.getEmail()).isPresent()) {
            throw new ResourceDuplicateException("Email address is already registered");
        }

        if (usuario.getContrasena() == null) {
            throw new BusinessRuleException("Password must not be null");
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setEstado(true);
        return usuarioRepositoryPort.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepositoryPort.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepositoryPort.findByEmail(email);
    }

    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepositoryPort.findAll(pageable);
    }

    public List<Usuario> listarVeterinariosActivos() {
        return usuarioRepositoryPort.findByRolAndEstado("VETERINARIO", true);
    }

    public Page<Usuario> listarVeterinariosActivos(Pageable pageable) {
        return usuarioRepositoryPort.findByRolAndEstado("VETERINARIO", true, pageable);
    }

    public List<Usuario> listarVeterinarios() {
        return usuarioRepositoryPort.findByRol("VETERINARIO");
    }

    public Page<Usuario> listarVeterinarios(Pageable pageable) {
        return usuarioRepositoryPort.findByRol("VETERINARIO", pageable);
    }

    @Transactional
    public Usuario cambiarEstado(Long id, Boolean estado) {
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        usuario.setEstado(estado);
        return usuarioRepositoryPort.save(usuario);
    }

    @Transactional
    public void incrementarTokenVersion(Long userId) {
        Usuario usuario = usuarioRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        usuario.setTokenVersion(usuario.getTokenVersion() == null ? 1 : usuario.getTokenVersion() + 1);
        usuarioRepositoryPort.save(usuario);
    }
}
