package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.persistence.entity.CitaEntity;
import com.petcare.backend.persistence.mapper.CitaMapper;
import com.petcare.backend.persistence.repository.CitaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Cita> findAll(Pageable pageable) {
        return citaRepository.findAll(pageable)
                .map(citaMapper::toModel);
    }

    @Override
    public Page<Cita> findByMascotaId(Long mascotaId, Pageable pageable) {
        return citaRepository.findByMascotaId(mascotaId, pageable)
                .map(citaMapper::toModel);
    }

    @Override
    public Page<Cita> findByVeterinarioId(Long veterinarioId, Pageable pageable) {
        return citaRepository.findByVeterinarioId(veterinarioId, pageable)
                .map(citaMapper::toModel);
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
