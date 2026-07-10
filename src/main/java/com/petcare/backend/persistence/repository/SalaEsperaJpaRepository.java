package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.SalaEsperaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaEsperaJpaRepository extends JpaRepository<SalaEsperaEntity, Long> {
    Optional<SalaEsperaEntity> findByCitaId(Long citaId);

    @EntityGraph(attributePaths = {"cita", "cita.veterinario", "cita.servicio", "mascota"})
    List<SalaEsperaEntity> findAllByOrderByFechaLlegadaAsc();

    List<SalaEsperaEntity> findByEstadoOrderByFechaLlegadaAsc(String estado);
}
