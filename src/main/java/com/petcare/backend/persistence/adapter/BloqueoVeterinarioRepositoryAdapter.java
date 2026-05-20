package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.port.BloqueoVeterinarioRepositoryPort;
import com.petcare.backend.persistence.entity.BloqueoVeterinarioEntity;
import com.petcare.backend.persistence.mapper.BloqueoVeterinarioMapper;
import com.petcare.backend.persistence.repository.BloqueoVeterinarioRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BloqueoVeterinarioRepositoryAdapter implements BloqueoVeterinarioRepositoryPort {

    private final BloqueoVeterinarioRepository repository;
    private final BloqueoVeterinarioMapper mapper;

    public BloqueoVeterinarioRepositoryAdapter(BloqueoVeterinarioRepository repository, BloqueoVeterinarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BloqueoVeterinario> findByVeterinarioId(Long veterinarioId) {
        return repository.findByVeterinarioId(veterinarioId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<BloqueoVeterinario> findByVeterinarioIdAndFecha(Long veterinarioId, LocalDate fecha) {
        return repository.findByVeterinarioIdAndFecha(veterinarioId, fecha).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BloqueoVeterinario> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public BloqueoVeterinario save(BloqueoVeterinario bloqueo) {
        BloqueoVeterinarioEntity entity = mapper.toEntity(bloqueo);
        BloqueoVeterinarioEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }
}
