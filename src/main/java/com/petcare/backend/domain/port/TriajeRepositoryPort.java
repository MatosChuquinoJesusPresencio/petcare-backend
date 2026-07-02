package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Triaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TriajeRepositoryPort {
    Triaje save(Triaje triaje);
    Optional<Triaje> findById(Long id);
    Optional<Triaje> findByCitaId(Long citaId);
    Page<Triaje> findAll(Pageable pageable);
    Page<Triaje> findByNivelUrgencia(String nivelUrgencia, Pageable pageable);
}
