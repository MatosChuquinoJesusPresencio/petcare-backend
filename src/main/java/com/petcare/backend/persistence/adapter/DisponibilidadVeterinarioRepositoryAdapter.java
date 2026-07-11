package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.port.DisponibilidadVeterinarioRepositoryPort;
import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import com.petcare.backend.persistence.mapper.DisponibilidadVeterinarioMapper;
import com.petcare.backend.persistence.repository.DisponibilidadVeterinarioJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DisponibilidadVeterinarioRepositoryAdapter implements DisponibilidadVeterinarioRepositoryPort {

    private final DisponibilidadVeterinarioJpaRepository disponibilidadVeterinarioJpaRepository;
    private final DisponibilidadVeterinarioMapper disponibilidadVeterinarioMapper;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public DisponibilidadVeterinarioRepositoryAdapter(DisponibilidadVeterinarioJpaRepository disponibilidadVeterinarioJpaRepository,
                                                      DisponibilidadVeterinarioMapper disponibilidadVeterinarioMapper,
                                                      UsuarioJpaRepository usuarioJpaRepository) {
        this.disponibilidadVeterinarioJpaRepository = disponibilidadVeterinarioJpaRepository;
        this.disponibilidadVeterinarioMapper = disponibilidadVeterinarioMapper;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public List<DisponibilidadVeterinario> findByVeterinarioId(Long veterinarioId) {
        return disponibilidadVeterinarioJpaRepository.findByVeterinarioId(veterinarioId).stream()
                .map(disponibilidadVeterinarioMapper::toDomain).toList();
    }

    @Override
    public List<DisponibilidadVeterinario> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana) {
        return disponibilidadVeterinarioJpaRepository.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemana).stream()
                .map(disponibilidadVeterinarioMapper::toDomain).toList();
    }

    @Override
    public Optional<DisponibilidadVeterinario> findById(Long id) {
        return disponibilidadVeterinarioJpaRepository.findById(id).map(disponibilidadVeterinarioMapper::toDomain);
    }

    @Override
    public DisponibilidadVeterinario save(DisponibilidadVeterinario disponibilidad) {
        DisponibilidadVeterinarioEntity entity = disponibilidadVeterinarioMapper.toEntity(disponibilidad);
        if (disponibilidad.getVeterinario() != null && disponibilidad.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(disponibilidad.getVeterinario().getId()));
        }
        DisponibilidadVeterinarioEntity saved = disponibilidadVeterinarioJpaRepository.save(entity);
        return disponibilidadVeterinarioMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        disponibilidadVeterinarioJpaRepository.deleteById(id);
    }
}
