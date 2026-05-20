package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.persistence.entity.DuenoEntity;
import com.petcare.backend.persistence.mapper.DuenoMapper;
import com.petcare.backend.persistence.repository.DuenoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DuenoRepositoryAdapter implements DuenoRepositoryPort {

    private final DuenoRepository duenoRepository;
    private final DuenoMapper duenoMapper;

    public DuenoRepositoryAdapter(DuenoRepository duenoRepository, DuenoMapper duenoMapper) {
        this.duenoRepository = duenoRepository;
        this.duenoMapper = duenoMapper;
    }

    @Override
    public Optional<Dueno> findById(Long id) {
        return duenoRepository.findById(id).map(duenoMapper::toModel);
    }

    @Override
    public Optional<Dueno> findByDni(String dni) {
        return duenoRepository.findByDni(dni).map(duenoMapper::toModel);
    }

    @Override
    public Optional<Dueno> findByEmail(String email) {
        return duenoRepository.findByEmail(email).map(duenoMapper::toModel);
    }

    @Override
    public Page<Dueno> findAll(Pageable pageable) {
        return duenoRepository.findAll(pageable)
                .map(duenoMapper::toModel);
    }

    @Override
    public Dueno save(Dueno dueno) {
        DuenoEntity entity = duenoMapper.toEntity(dueno);
        DuenoEntity savedEntity = duenoRepository.save(entity);
        return duenoMapper.toModel(savedEntity);
    }
}
