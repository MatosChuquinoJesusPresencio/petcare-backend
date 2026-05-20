package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.ContactoEmergencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ContactoEmergenciaRepositoryPort {
    Page<ContactoEmergencia> findByDuenoId(Long duenoId, Pageable pageable);
    Optional<ContactoEmergencia> findById(Long id);
    ContactoEmergencia save(ContactoEmergencia contacto);
    void deleteById(Long id);
}
