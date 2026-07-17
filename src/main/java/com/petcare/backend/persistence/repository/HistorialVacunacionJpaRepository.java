package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.HistorialVacunacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HistorialVacunacionJpaRepository extends JpaRepository<HistorialVacunacionEntity, Long> {
    List<HistorialVacunacionEntity> findByMascotaIdOrderByFechaAplicacionDesc(Long mascotaId);
    List<HistorialVacunacionEntity> findByProximaDosisBetweenOrderByProximaDosisAsc(LocalDate desde, LocalDate hasta);
}
