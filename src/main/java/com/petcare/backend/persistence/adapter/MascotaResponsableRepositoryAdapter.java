package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.MascotaResponsable;
import com.petcare.backend.domain.port.MascotaResponsableRepositoryPort;
import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import com.petcare.backend.persistence.mapper.MascotaResponsableMapper;
import com.petcare.backend.persistence.repository.DuenoJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaResponsableJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MascotaResponsableRepositoryAdapter implements MascotaResponsableRepositoryPort {

    private final MascotaResponsableJpaRepository mascotaResponsableJpaRepository;
    private final MascotaResponsableMapper mascotaResponsableMapper;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final DuenoJpaRepository duenoJpaRepository;

    public MascotaResponsableRepositoryAdapter(MascotaResponsableJpaRepository mascotaResponsableJpaRepository,
                                               MascotaResponsableMapper mascotaResponsableMapper,
                                               MascotaJpaRepository mascotaJpaRepository,
                                               DuenoJpaRepository duenoJpaRepository) {
        this.mascotaResponsableJpaRepository = mascotaResponsableJpaRepository;
        this.mascotaResponsableMapper = mascotaResponsableMapper;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.duenoJpaRepository = duenoJpaRepository;
    }

    @Override
    public List<MascotaResponsable> findByMascotaId(Long mascotaId) {
        return mascotaResponsableJpaRepository.findByMascotaId(mascotaId).stream()
                .map(mascotaResponsableMapper::toDomain).toList();
    }

    @Override
    public Page<MascotaResponsable> findByDuenoId(Long duenoId, Pageable pageable) {
        return mascotaResponsableJpaRepository.findByDuenoId(duenoId, pageable)
                .map(mascotaResponsableMapper::toDomain);
    }

    @Override
    public Optional<MascotaResponsable> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId) {
        return mascotaResponsableJpaRepository.findByMascotaIdAndDuenoId(mascotaId, duenoId)
                .map(mascotaResponsableMapper::toDomain);
    }

    @Override
    public MascotaResponsable save(MascotaResponsable mascotaResponsable) {
        MascotaResponsableEntity entity = mascotaResponsableMapper.toEntity(mascotaResponsable);
        if (mascotaResponsable.getMascota() != null && mascotaResponsable.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(mascotaResponsable.getMascota().getId()));
        }
        if (mascotaResponsable.getDueno() != null && mascotaResponsable.getDueno().getId() != null) {
            entity.setDueno(duenoJpaRepository.getReferenceById(mascotaResponsable.getDueno().getId()));
        }
        MascotaResponsableEntity saved = mascotaResponsableJpaRepository.save(entity);
        return mascotaResponsableMapper.toDomain(saved);
    }
}
