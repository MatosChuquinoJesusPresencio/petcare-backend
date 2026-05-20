package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.CitaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {
    Page<CitaEntity> findByMascotaId(Long mascotaId, Pageable pageable);
    Page<CitaEntity> findByVeterinarioId(Long veterinarioId, Pageable pageable);
    java.util.List<CitaEntity> findByVeterinarioIdAndFechaHoraBetween(Long veterinarioId, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
