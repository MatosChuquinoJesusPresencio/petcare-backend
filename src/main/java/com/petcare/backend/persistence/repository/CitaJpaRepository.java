package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.CitaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

public interface CitaJpaRepository extends JpaRepository<CitaEntity, Long>, JpaSpecificationExecutor<CitaEntity> {

    Page<CitaEntity> findByMascotaId(Long mascotaId, Pageable pageable);

    Page<CitaEntity> findByVeterinarioId(Long veterinarioId, Pageable pageable);

    @EntityGraph(attributePaths = {"mascota", "veterinario", "servicio", "creadoPor", "actualizadoPor"})
    List<CitaEntity> findByVeterinarioIdAndFechaHoraBetweenOrderByFechaHoraAsc(Long veterinarioId, Instant start, Instant end);
}
