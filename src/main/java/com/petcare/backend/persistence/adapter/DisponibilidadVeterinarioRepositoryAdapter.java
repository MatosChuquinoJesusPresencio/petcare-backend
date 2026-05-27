package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.port.DisponibilidadVeterinarioRepositoryPort;
import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import com.petcare.backend.persistence.mapper.DisponibilidadVeterinarioMapper;
import com.petcare.backend.persistence.repository.DisponibilidadVeterinarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DisponibilidadVeterinarioRepositoryAdapter implements DisponibilidadVeterinarioRepositoryPort {

    private final DisponibilidadVeterinarioRepository disponibilidadVeterinarioRepository;
    private final DisponibilidadVeterinarioMapper disponibilidadVeterinarioMapper;

    public DisponibilidadVeterinarioRepositoryAdapter(DisponibilidadVeterinarioRepository disponibilidadVeterinarioRepository, DisponibilidadVeterinarioMapper disponibilidadVeterinarioMapper) {
        this.disponibilidadVeterinarioRepository = disponibilidadVeterinarioRepository;
        this.disponibilidadVeterinarioMapper = disponibilidadVeterinarioMapper;
    }

    @Override
    public List<DisponibilidadVeterinario> findByVeterinarioId(Long veterinarioId) {
        return disponibilidadVeterinarioRepository.findByVeterinarioId(veterinarioId).stream()
                .map(disponibilidadVeterinarioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DisponibilidadVeterinario> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana) {
        return disponibilidadVeterinarioRepository.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemana).stream()
                .map(disponibilidadVeterinarioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DisponibilidadVeterinario> findById(Long id) {
        return disponibilidadVeterinarioRepository.findById(id).map(disponibilidadVeterinarioMapper::toModel);
    }

    @Override
    public DisponibilidadVeterinario save(DisponibilidadVeterinario disponibilidad) {
        DisponibilidadVeterinarioEntity entity = disponibilidadVeterinarioMapper.toEntity(disponibilidad);
        DisponibilidadVeterinarioEntity savedEntity = disponibilidadVeterinarioRepository.save(entity);
        return disponibilidadVeterinarioMapper.toModel(savedEntity);
    }
}
