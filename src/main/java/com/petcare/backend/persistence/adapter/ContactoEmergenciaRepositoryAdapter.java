package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.port.ContactoEmergenciaRepositoryPort;
import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import com.petcare.backend.persistence.mapper.ContactoEmergenciaMapper;
import com.petcare.backend.persistence.repository.ContactoEmergenciaJpaRepository;
import com.petcare.backend.persistence.repository.DuenoJpaRepository;
import com.petcare.backend.persistence.specification.ContactoEmergenciaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContactoEmergenciaRepositoryAdapter implements ContactoEmergenciaRepositoryPort {

    private final ContactoEmergenciaJpaRepository contactoEmergenciaJpaRepository;
    private final ContactoEmergenciaMapper contactoEmergenciaMapper;
    private final DuenoJpaRepository duenoJpaRepository;

    public ContactoEmergenciaRepositoryAdapter(ContactoEmergenciaJpaRepository contactoEmergenciaJpaRepository,
                                               ContactoEmergenciaMapper contactoEmergenciaMapper,
                                               DuenoJpaRepository duenoJpaRepository) {
        this.contactoEmergenciaJpaRepository = contactoEmergenciaJpaRepository;
        this.contactoEmergenciaMapper = contactoEmergenciaMapper;
        this.duenoJpaRepository = duenoJpaRepository;
    }

    @Override
    public Page<ContactoEmergencia> findAll(Long duenoId, String nombre, String telefono, String relacion, Pageable pageable) {
        return contactoEmergenciaJpaRepository.findAll(ContactoEmergenciaSpecification.withFilters(duenoId, nombre, telefono, relacion), pageable)
                .map(contactoEmergenciaMapper::toDomain);
    }

    @Override
    public Optional<ContactoEmergencia> findById(Long id) {
        return contactoEmergenciaJpaRepository.findById(id).map(contactoEmergenciaMapper::toDomain);
    }

    @Override
    public ContactoEmergencia save(ContactoEmergencia contacto) {
        ContactoEmergenciaEntity entity = contactoEmergenciaMapper.toEntity(contacto);
        if (contacto.getDueno() != null && contacto.getDueno().getId() != null) {
            entity.setDueno(duenoJpaRepository.getReferenceById(contacto.getDueno().getId()));
        }
        ContactoEmergenciaEntity saved = contactoEmergenciaJpaRepository.save(entity);
        return contactoEmergenciaMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        contactoEmergenciaJpaRepository.deleteById(id);
    }
}
