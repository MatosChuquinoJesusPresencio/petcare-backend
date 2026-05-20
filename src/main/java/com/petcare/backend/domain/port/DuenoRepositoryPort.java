package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Dueno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface DuenoRepositoryPort {
    Optional<Dueno> findById(Long id);
    Optional<Dueno> findByDni(String dni);
    Optional<Dueno> findByEmail(String email);
    Page<Dueno> findAll(Pageable pageable);
    Dueno save(Dueno dueno);
}
