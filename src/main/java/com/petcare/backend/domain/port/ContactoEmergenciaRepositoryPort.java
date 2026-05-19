package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.ContactoEmergencia;
import java.util.List;
import java.util.Optional;

public interface ContactoEmergenciaRepositoryPort {
    List<ContactoEmergencia> findByDuenoId(Long duenoId);
    Optional<ContactoEmergencia> findById(Long id);
    ContactoEmergencia save(ContactoEmergencia contacto);
    void deleteById(Long id);
}
