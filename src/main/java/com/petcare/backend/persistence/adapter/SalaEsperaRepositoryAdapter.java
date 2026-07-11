package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.port.SalaEsperaRepositoryPort;
import com.petcare.backend.persistence.entity.SalaEsperaEntity;
import com.petcare.backend.persistence.mapper.SalaEsperaMapper;
import com.petcare.backend.persistence.repository.CitaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.SalaEsperaJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SalaEsperaRepositoryAdapter implements SalaEsperaRepositoryPort {

    private final SalaEsperaJpaRepository salaEsperaJpaRepository;
    private final SalaEsperaMapper salaEsperaMapper;
    private final CitaJpaRepository citaJpaRepository;
    private final MascotaJpaRepository mascotaJpaRepository;

    public SalaEsperaRepositoryAdapter(SalaEsperaJpaRepository salaEsperaJpaRepository,
                                       SalaEsperaMapper salaEsperaMapper,
                                       CitaJpaRepository citaJpaRepository,
                                       MascotaJpaRepository mascotaJpaRepository) {
        this.salaEsperaJpaRepository = salaEsperaJpaRepository;
        this.salaEsperaMapper = salaEsperaMapper;
        this.citaJpaRepository = citaJpaRepository;
        this.mascotaJpaRepository = mascotaJpaRepository;
    }

    @Override
    public SalaEspera save(SalaEspera salaEspera) {
        SalaEsperaEntity entity = salaEsperaMapper.toEntity(salaEspera);
        if (salaEspera.getCita() != null && salaEspera.getCita().getId() != null) {
            entity.setCita(citaJpaRepository.getReferenceById(salaEspera.getCita().getId()));
        }
        if (salaEspera.getMascota() != null && salaEspera.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(salaEspera.getMascota().getId()));
        }
        SalaEsperaEntity saved = salaEsperaJpaRepository.save(entity);
        return salaEsperaMapper.toDomain(saved);
    }

    @Override
    public Optional<SalaEspera> findById(Long id) {
        return salaEsperaJpaRepository.findById(id).map(salaEsperaMapper::toDomain);
    }

    @Override
    public Optional<SalaEspera> findByCitaId(Long citaId) {
        return salaEsperaJpaRepository.findByCitaId(citaId).map(salaEsperaMapper::toDomain);
    }

    @Override
    public Page<SalaEspera> findAllByOrderByFechaLlegadaAsc(Pageable pageable) {
        return salaEsperaJpaRepository.findAllByOrderByFechaLlegadaAsc(pageable)
                .map(salaEsperaMapper::toDomain);
    }

    @Override
    public Page<SalaEspera> findByEstadoOrderByFechaLlegadaAsc(String estado, Pageable pageable) {
        return salaEsperaJpaRepository.findByEstadoOrderByFechaLlegadaAsc(estado, pageable)
                .map(salaEsperaMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        salaEsperaJpaRepository.deleteById(id);
    }
}
