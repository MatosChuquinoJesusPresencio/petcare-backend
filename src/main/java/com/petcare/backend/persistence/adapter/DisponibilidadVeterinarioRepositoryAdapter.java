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

    private final DisponibilidadVeterinarioRepository repository;
    private final DisponibilidadVeterinarioMapper mapper;

    public DisponibilidadVeterinarioRepositoryAdapter(DisponibilidadVeterinarioRepository repository, DisponibilidadVeterinarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DisponibilidadVeterinario> findByVeterinarioId(Long veterinarioId) {
        return repository.findByVeterinarioId(veterinarioId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DisponibilidadVeterinario> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana) {
        return repository.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemana).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DisponibilidadVeterinario> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public DisponibilidadVeterinario save(DisponibilidadVeterinario disponibilidad) {
        DisponibilidadVeterinarioEntity entity = mapper.toEntity(disponibilidad);
        DisponibilidadVeterinarioEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }
}
