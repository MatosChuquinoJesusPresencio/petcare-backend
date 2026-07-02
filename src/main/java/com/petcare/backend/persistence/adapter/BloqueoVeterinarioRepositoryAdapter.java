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

    private final BloqueoVeterinarioRepository bloqueVeterinarioRepository;
    private final BloqueoVeterinarioMapper bloqueVeterinarioMapper;

    public BloqueoVeterinarioRepositoryAdapter(BloqueoVeterinarioRepository bloqueVeterinarioRepository, BloqueoVeterinarioMapper bloqueVeterinarioMapper) {
        this.bloqueVeterinarioRepository = bloqueVeterinarioRepository;
        this.bloqueVeterinarioMapper = bloqueVeterinarioMapper;
    }

    @Override
    public List<BloqueoVeterinario> findByVeterinarioId(Long veterinarioId) {
        return bloqueVeterinarioRepository.findByVeterinarioId(veterinarioId).stream()
                .map(bloqueVeterinarioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<BloqueoVeterinario> findByVeterinarioIdAndFecha(Long veterinarioId, LocalDate fecha) {
        return bloqueVeterinarioRepository.findByVeterinarioIdAndFecha(veterinarioId, fecha).stream()
                .map(bloqueVeterinarioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BloqueoVeterinario> findById(Long id) {
        return bloqueVeterinarioRepository.findById(id).map(bloqueVeterinarioMapper::toModel);
    }

    @Override
    public BloqueoVeterinario save(BloqueoVeterinario bloqueo) {
        BloqueoVeterinarioEntity entity = bloqueVeterinarioMapper.toEntity(bloqueo);
        BloqueoVeterinarioEntity savedEntity = bloqueVeterinarioRepository.save(entity);
        return bloqueVeterinarioMapper.toModel(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        bloqueVeterinarioRepository.deleteById(id);
    }
}
