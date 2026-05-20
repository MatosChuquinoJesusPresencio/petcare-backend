package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.persistence.entity.MascotaEntity;
import com.petcare.backend.persistence.mapper.MascotaMapper;
import com.petcare.backend.persistence.repository.MascotaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MascotaRepositoryAdapter implements MascotaRepositoryPort {

    private final MascotaRepository mascotaRepository;
    private final MascotaMapper mascotaMapper;

    public MascotaRepositoryAdapter(MascotaRepository mascotaRepository, MascotaMapper mascotaMapper) {
        this.mascotaRepository = mascotaRepository;
        this.mascotaMapper = mascotaMapper;
    }

    @Override
    public Optional<Mascota> findById(Long id) {
        return mascotaRepository.findById(id).map(mascotaMapper::toModel);
    }

    @Override
    public Optional<Mascota> findByMicrochip(String microchip) {
        return mascotaRepository.findByMicrochip(microchip).map(mascotaMapper::toModel);
    }

    @Override
    public List<Mascota> findAll() {
        return mascotaRepository.findAll().stream()
                .map(mascotaMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Mascota save(Mascota mascota) {
        MascotaEntity entity = mascotaMapper.toEntity(mascota);
        MascotaEntity savedEntity = mascotaRepository.save(entity);
        return mascotaMapper.toModel(savedEntity);
    }
}
