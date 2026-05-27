package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.MascotaResponsable;
import com.petcare.backend.domain.port.MascotaResponsableRepositoryPort;
import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import com.petcare.backend.persistence.mapper.MascotaResponsableMapper;
import com.petcare.backend.persistence.repository.MascotaResponsableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MascotaResponsableRepositoryAdapter implements MascotaResponsableRepositoryPort {

    private final MascotaResponsableRepository mascotaResponsableRepository;
    private final MascotaResponsableMapper mascotaResponsableMapper;

    public MascotaResponsableRepositoryAdapter(MascotaResponsableRepository mascotaResponsableRepository, MascotaResponsableMapper mascotaResponsableMapper) {
        this.mascotaResponsableRepository = mascotaResponsableRepository;
        this.mascotaResponsableMapper = mascotaResponsableMapper;
    }

    @Override
    public List<MascotaResponsable> findByMascotaId(Long mascotaId) {
        return mascotaResponsableRepository.findByMascotaId(mascotaId).stream()
                .map(mascotaResponsableMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MascotaResponsable> findByDuenoId(Long duenoId, Pageable pageable) {
        return mascotaResponsableRepository.findByDuenoId(duenoId, pageable)
                .map(mascotaResponsableMapper::toModel);
    }

    @Override
    public Optional<MascotaResponsable> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId) {
        return mascotaResponsableRepository.findByMascotaIdAndDuenoId(mascotaId, duenoId).map(mascotaResponsableMapper::toModel);
    }

    @Override
    public MascotaResponsable save(MascotaResponsable mascotaResponsable) {
        MascotaResponsableEntity entity = mascotaResponsableMapper.toEntity(mascotaResponsable);
        MascotaResponsableEntity savedEntity = mascotaResponsableRepository.save(entity);
        return mascotaResponsableMapper.toModel(savedEntity);
    }

    @Override
    @Transactional
    public void deleteByMascotaIdAndDuenoId(Long mascotaId, Long duenoId) {
        mascotaResponsableRepository.deleteByMascotaIdAndDuenoId(mascotaId, duenoId);
    }
}
