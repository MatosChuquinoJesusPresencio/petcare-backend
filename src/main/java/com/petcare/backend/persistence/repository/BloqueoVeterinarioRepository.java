package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.BloqueoVeterinarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueoVeterinarioRepository extends JpaRepository<BloqueoVeterinarioEntity, Long> {
    java.util.List<BloqueoVeterinarioEntity> findByVeterinarioId(Long veterinarioId);
    java.util.List<BloqueoVeterinarioEntity> findByVeterinarioIdAndFecha(Long veterinarioId, java.time.LocalDate fecha);
}
