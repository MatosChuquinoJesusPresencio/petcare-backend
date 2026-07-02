package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.SalaEspera;

import java.util.List;
import java.util.Optional;

public interface SalaEsperaRepositoryPort {
    SalaEspera save(SalaEspera salaEspera);
    Optional<SalaEspera> findById(Long id);
    Optional<SalaEspera> findByCitaId(Long citaId);
    List<SalaEspera> findAllByOrderByFechaLlegadaAsc();
    List<SalaEspera> findByEstadoOrderByFechaLlegadaAsc(String estado);
    void deleteById(Long id);
}
