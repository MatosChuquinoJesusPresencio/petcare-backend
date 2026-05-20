package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.port.ContactoEmergenciaRepositoryPort;
import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import com.petcare.backend.persistence.mapper.ContactoEmergenciaMapper;
import com.petcare.backend.persistence.repository.ContactoEmergenciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContactoEmergenciaRepositoryAdapter implements ContactoEmergenciaRepositoryPort {

    private final ContactoEmergenciaRepository contactoRepository;
    private final ContactoEmergenciaMapper contactoMapper;

    public ContactoEmergenciaRepositoryAdapter(ContactoEmergenciaRepository contactoRepository, ContactoEmergenciaMapper contactoMapper) {
        this.contactoRepository = contactoRepository;
        this.contactoMapper = contactoMapper;
    }

    @Override
    public Page<ContactoEmergencia> findByDuenoId(Long duenoId, Pageable pageable) {
        return contactoRepository.findByDuenoId(duenoId, pageable)
                .map(contactoMapper::toModel);
    }

    @Override
    public Optional<ContactoEmergencia> findById(Long id) {
        return contactoRepository.findById(id).map(contactoMapper::toModel);
    }

    @Override
    public ContactoEmergencia save(ContactoEmergencia contacto) {
        ContactoEmergenciaEntity entity = contactoMapper.toEntity(contacto);
        ContactoEmergenciaEntity savedEntity = contactoRepository.save(entity);
        return contactoMapper.toModel(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        contactoRepository.deleteById(id);
    }
}
