package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.BloqueoVeterinarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BloqueoVeterinarioJpaRepository extends JpaRepository<BloqueoVeterinarioEntity, Long> {
    List<BloqueoVeterinarioEntity> findByVeterinarioId(Long veterinarioId);

    List<BloqueoVeterinarioEntity> findByVeterinarioIdAndFecha(Long veterinarioId, LocalDate fecha);
}
