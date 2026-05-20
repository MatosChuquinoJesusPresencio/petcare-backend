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

    private final MascotaResponsableRepository repository;
    private final MascotaResponsableMapper mapper;

    public MascotaResponsableRepositoryAdapter(MascotaResponsableRepository repository, MascotaResponsableMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<MascotaResponsable> findByMascotaId(Long mascotaId) {
        return repository.findByMascotaId(mascotaId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MascotaResponsable> findByDuenoId(Long duenoId, Pageable pageable) {
        return repository.findByDuenoId(duenoId, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Optional<MascotaResponsable> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId) {
        return repository.findByMascotaIdAndDuenoId(mascotaId, duenoId).map(mapper::toModel);
    }

    @Override
    public MascotaResponsable save(MascotaResponsable mascotaResponsable) {
        MascotaResponsableEntity entity = mapper.toEntity(mascotaResponsable);
        MascotaResponsableEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    @Transactional
    public void deleteByMascotaIdAndDuenoId(Long mascotaId, Long duenoId) {
        repository.deleteByMascotaIdAndDuenoId(mascotaId, duenoId);
    }
}
