package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.persistence.entity.DuenoEntity;
import com.petcare.backend.persistence.mapper.DuenoMapper;
import com.petcare.backend.persistence.repository.DuenoJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import com.petcare.backend.persistence.specification.DuenoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DuenoRepositoryAdapter implements DuenoRepositoryPort {

    private final DuenoJpaRepository duenoJpaRepository;
    private final DuenoMapper duenoMapper;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public DuenoRepositoryAdapter(DuenoJpaRepository duenoJpaRepository, DuenoMapper duenoMapper, UsuarioJpaRepository usuarioJpaRepository) {
        this.duenoJpaRepository = duenoJpaRepository;
        this.duenoMapper = duenoMapper;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<Dueno> findById(Long id) {
        return duenoJpaRepository.findById(id).map(duenoMapper::toDomain);
    }

    @Override
    public Optional<Dueno> findByDni(String dni) {
        return duenoJpaRepository.findByDni(dni).map(duenoMapper::toDomain);
    }

    @Override
    public Optional<Dueno> findByEmail(String email) {
        return duenoJpaRepository.findByUsuarioEmail(email).map(duenoMapper::toDomain);
    }

    @Override
    public Page<Dueno> findAll(Boolean soloActivos, String nombre, String dni, Pageable pageable) {
        return duenoJpaRepository.findAll(DuenoSpecification.withFilters(soloActivos, nombre, dni), pageable).map(duenoMapper::toDomain);
    }

    @Override
    public Dueno save(Dueno dueno) {
        DuenoEntity entity = duenoMapper.toEntity(dueno);
        if (dueno.getUsuario() != null && dueno.getUsuario().getId() != null) {
            entity.setUsuario(usuarioJpaRepository.getReferenceById(dueno.getUsuario().getId()));
        }
        DuenoEntity saved = duenoJpaRepository.save(entity);
        return duenoMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        duenoJpaRepository.deleteById(id);
    }
}
