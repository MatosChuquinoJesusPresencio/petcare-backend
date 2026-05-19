package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Dueno;
import java.util.Optional;
import java.util.List;

public interface DuenoRepositoryPort {
    Optional<Dueno> findById(Long id);
    Optional<Dueno> findByDni(String dni);
    Optional<Dueno> findByEmail(String email);
    List<Dueno> findAll();
    Dueno save(Dueno dueno);
}
