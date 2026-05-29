package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.persistence.entity.MascotaEntity;
import com.petcare.backend.persistence.mapper.MascotaMapper;
import com.petcare.backend.persistence.repository.MascotaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public Page<Mascota> findAll(Pageable pageable) {
        return mascotaRepository.findAll(pageable)
                .map(mascotaMapper::toModel);
    }

    @Override
    public Page<Mascota> findAll(String nombre, String especie, String raza, String sexo, Boolean activo, Long duenoId, Pageable pageable) {
        var spec = com.petcare.backend.persistence.specification.MascotaSpecification.conFiltros(nombre, especie, raza, sexo, activo, duenoId);
        return mascotaRepository.findAll(spec, pageable)
                .map(mascotaMapper::toModel);
    }

    @Override
    public Mascota save(Mascota mascota) {
        MascotaEntity entity = mascotaMapper.toEntity(mascota);
        MascotaEntity savedEntity = mascotaRepository.save(entity);
        return mascotaMapper.toModel(savedEntity);
    }
}
