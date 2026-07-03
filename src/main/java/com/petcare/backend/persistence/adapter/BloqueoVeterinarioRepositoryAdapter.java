package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.port.BloqueoVeterinarioRepositoryPort;
import com.petcare.backend.persistence.entity.BloqueoVeterinarioEntity;
import com.petcare.backend.persistence.mapper.BloqueoVeterinarioMapper;
import com.petcare.backend.persistence.repository.BloqueoVeterinarioJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class BloqueoVeterinarioRepositoryAdapter implements BloqueoVeterinarioRepositoryPort {

    private final BloqueoVeterinarioJpaRepository bloqueoVeterinarioJpaRepository;
    private final BloqueoVeterinarioMapper bloqueoVeterinarioMapper;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public BloqueoVeterinarioRepositoryAdapter(BloqueoVeterinarioJpaRepository bloqueoVeterinarioJpaRepository,
                                               BloqueoVeterinarioMapper bloqueoVeterinarioMapper,
                                               UsuarioJpaRepository usuarioJpaRepository) {
        this.bloqueoVeterinarioJpaRepository = bloqueoVeterinarioJpaRepository;
        this.bloqueoVeterinarioMapper = bloqueoVeterinarioMapper;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public List<BloqueoVeterinario> findByVeterinarioId(Long veterinarioId) {
        return bloqueoVeterinarioJpaRepository.findByVeterinarioId(veterinarioId).stream()
                .map(bloqueoVeterinarioMapper::toDomain).toList();
    }

    @Override
    public List<BloqueoVeterinario> findByVeterinarioIdAndFecha(Long veterinarioId, LocalDate fecha) {
        return bloqueoVeterinarioJpaRepository.findByVeterinarioIdAndFecha(veterinarioId, fecha).stream()
                .map(bloqueoVeterinarioMapper::toDomain).toList();
    }

    @Override
    public Optional<BloqueoVeterinario> findById(Long id) {
        return bloqueoVeterinarioJpaRepository.findById(id).map(bloqueoVeterinarioMapper::toDomain);
    }

    @Override
    public BloqueoVeterinario save(BloqueoVeterinario bloqueo) {
        BloqueoVeterinarioEntity entity = bloqueoVeterinarioMapper.toEntity(bloqueo);
        if (bloqueo.getVeterinario() != null && bloqueo.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(bloqueo.getVeterinario().getId()));
        }
        BloqueoVeterinarioEntity saved = bloqueoVeterinarioJpaRepository.save(entity);
        return bloqueoVeterinarioMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        bloqueoVeterinarioJpaRepository.deleteById(id);
    }
}
