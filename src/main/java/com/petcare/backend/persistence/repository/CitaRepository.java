package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.CitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {
    java.util.List<CitaEntity> findByMascotaId(Long mascotaId);
    java.util.List<CitaEntity> findByVeterinarioId(Long veterinarioId);
    java.util.List<CitaEntity> findByVeterinarioIdAndFechaHoraBetween(Long veterinarioId, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
