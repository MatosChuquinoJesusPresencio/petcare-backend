package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.persistence.mapper.MascotaMapper;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.specification.MascotaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MascotaRepositoryAdapter implements MascotaRepositoryPort {

    private final MascotaJpaRepository mascotaJpaRepository;
    private final MascotaMapper mascotaMapper;

    public MascotaRepositoryAdapter(MascotaJpaRepository mascotaJpaRepository, MascotaMapper mascotaMapper) {
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.mascotaMapper = mascotaMapper;
    }

    @Override
    public Optional<Mascota> findById(Long id) {
        return mascotaJpaRepository.findById(id).map(mascotaMapper::toDomain);
    }

    @Override
    public Optional<Mascota> findByMicrochip(String microchip) {
        return mascotaJpaRepository.findByMicrochip(microchip).map(mascotaMapper::toDomain);
    }

    @Override
    public Page<Mascota> findAll(Pageable pageable) {
        return mascotaJpaRepository.findAll(pageable).map(mascotaMapper::toDomain);
    }

    @Override
    public Page<Mascota> findAll(String nombre, String especie, String raza, String genero, Boolean estado, Long duenoId, Pageable pageable) {
        return mascotaJpaRepository.findAll(MascotaSpecification.withFilters(nombre, especie, raza, genero, estado, duenoId), pageable)
                .map(mascotaMapper::toDomain);
    }

    @Override
    public Mascota save(Mascota mascota) {
        return mascotaMapper.toDomain(mascotaJpaRepository.save(mascotaMapper.toEntity(mascota)));
    }

    @Override
    public void deleteById(Long id) {
        mascotaJpaRepository.deleteById(id);
    }
}
