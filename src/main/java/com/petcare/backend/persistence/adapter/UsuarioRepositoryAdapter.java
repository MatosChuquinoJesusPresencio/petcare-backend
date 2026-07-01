package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.persistence.entity.UsuarioEntity;
import com.petcare.backend.persistence.mapper.UsuarioMapper;
import com.petcare.backend.persistence.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioRepositoryAdapter(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toModel);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username).map(usuarioMapper::toModel);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuarioMapper::toModel);
    }

    @Override
    public Page<Usuario> findAll(Boolean soloActivos, String rol, Pageable pageable) {
        var spec = com.petcare.backend.persistence.specification.UsuarioSpecification.conFiltros(soloActivos, rol);
        return usuarioRepository.findAll(spec, pageable)
                .map(usuarioMapper::toModel);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> findByRolAndActivo(String rol, Boolean activo) {
        return usuarioRepository.findByRolAndActivo(rol, activo).stream()
                .map(usuarioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = usuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = usuarioRepository.save(entity);
        return usuarioMapper.toModel(savedEntity);
    }
}
