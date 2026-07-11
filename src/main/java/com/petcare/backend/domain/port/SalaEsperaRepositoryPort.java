package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.SalaEspera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SalaEsperaRepositoryPort {
    SalaEspera save(SalaEspera salaEspera);
    Optional<SalaEspera> findById(Long id);
    Optional<SalaEspera> findByCitaId(Long citaId);
    Page<SalaEspera> findAllByOrderByFechaLlegadaAsc(Pageable pageable);
    Page<SalaEspera> findByEstadoOrderByFechaLlegadaAsc(String estado, Pageable pageable);
    void deleteById(Long id);
}
