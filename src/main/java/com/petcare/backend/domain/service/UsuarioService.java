package com.petcare.backend.domain.service;

import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.RefreshTokenRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {
    private static final Set<String> ROLES_VALIDOS = Set.of(
            "ADMINISTRADOR",
            "ASISTENTE",
            "VETERINARIO",
            "DUENO"
    );

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
                          RefreshTokenRepositoryPort refreshTokenRepositoryPort,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        validarDuplicados(null, usuario.getUsername(), usuario.getEmail());
        usuario.setRol(normalizarRol(usuario.getRol()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepositoryPort.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validarDuplicados(id, usuarioDetalles.getUsername(), usuarioDetalles.getEmail());

        usuario.setUsername(usuarioDetalles.getUsername());
        usuario.setNombre(usuarioDetalles.getNombre());
        usuario.setApellido(usuarioDetalles.getApellido());
        usuario.setEmail(usuarioDetalles.getEmail());
        usuario.setTelefono(usuarioDetalles.getTelefono());
        usuario.setRol(normalizarRol(usuarioDetalles.getRol()));

        if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDetalles.getPassword()));
        }

        return usuarioRepositoryPort.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepositoryPort.findById(id);
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepositoryPort.findByUsername(username);
    }

    public Page<Usuario> listar(Boolean soloActivos, String rol, Pageable pageable) {
        return usuarioRepositoryPort.findAll(soloActivos, rol, pageable);
    }

    public List<Usuario> listarVeterinariosActivos() {
        return usuarioRepositoryPort.findByRolAndActivo("VETERINARIO", true);
    }

    public Usuario cambiarActivo(Long id) {
        Usuario usuario = usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        usuario.setActivo(!usuario.getActivo());
        return usuarioRepositoryPort.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        refreshTokenRepositoryPort.deleteByUsuarioId(id);

        try {
            usuarioRepositoryPort.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("User cannot be deleted because it is associated with other records");
        }
    }

    private void validarDuplicados(Long usuarioId, String username, String email) {
        usuarioRepositoryPort.findByUsername(username).ifPresent(existing -> {
            if (usuarioId == null || !existing.getId().equals(usuarioId)) {
                throw new ResourceDuplicateException("Username is already registered");
            }
        });

        usuarioRepositoryPort.findByEmail(email).ifPresent(existing -> {
            if (usuarioId == null || !existing.getId().equals(usuarioId)) {
                throw new ResourceDuplicateException("Email address is already registered");
            }
        });
    }

    private String normalizarRol(String rol) {
        String rolNormalizado = rol == null ? null : rol.trim().toUpperCase();
        if (rolNormalizado == null || !ROLES_VALIDOS.contains(rolNormalizado)) {
            throw new BusinessRuleException("Role is not valid");
        }
        return rolNormalizado;
    }
}
