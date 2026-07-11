package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.persistence.mapper.UsuarioMapper;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository, UsuarioMapper usuarioMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioJpaRepository.findById(id).map(usuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioJpaRepository.findByEmail(email).map(usuarioMapper::toDomain);
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioJpaRepository.findAll(pageable).map(usuarioMapper::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioMapper.toDomain(usuarioJpaRepository.save(usuarioMapper.toEntity(usuario)));
    }

    @Override
    public List<Usuario> findByRolAndEstado(String rol, Boolean estado) {
        return usuarioJpaRepository.findByRolAndEstado(rol, estado).stream().map(usuarioMapper::toDomain).toList();
    }

    @Override
    public Page<Usuario> findByRolAndEstado(String rol, Boolean estado, Pageable pageable) {
        return usuarioJpaRepository.findByRolAndEstado(rol, estado, pageable).map(usuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> findByRol(String rol) {
        return usuarioJpaRepository.findByRol(rol).stream().map(usuarioMapper::toDomain).toList();
    }

    @Override
    public Page<Usuario> findByRol(String rol, Pageable pageable) {
        return usuarioJpaRepository.findByRol(rol, pageable).map(usuarioMapper::toDomain);
    }
}
