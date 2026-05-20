package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.persistence.entity.CitaEntity;
import com.petcare.backend.persistence.mapper.CitaMapper;
import com.petcare.backend.persistence.repository.CitaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CitaRepositoryAdapter implements CitaRepositoryPort {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    public CitaRepositoryAdapter(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    @Override
    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id).map(citaMapper::toModel);
    }

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll().stream()
                .map(citaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cita> findByMascotaId(Long mascotaId) {
        return citaRepository.findByMascotaId(mascotaId).stream()
                .map(citaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cita> findByVeterinarioId(Long veterinarioId) {
        return citaRepository.findByVeterinarioId(veterinarioId).stream()
                .map(citaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cita> findByVeterinarioIdAndFechaHoraBetween(Long veterinarioId, LocalDateTime start, LocalDateTime end) {
        return citaRepository.findByVeterinarioIdAndFechaHoraBetween(veterinarioId, start, end).stream()
                .map(citaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Cita save(Cita cita) {
        CitaEntity entity = citaMapper.toEntity(cita);
        CitaEntity savedEntity = citaRepository.save(entity);
        return citaMapper.toModel(savedEntity);
    }
}
