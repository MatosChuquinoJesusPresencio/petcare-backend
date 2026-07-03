package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadVeterinarioJpaRepository extends JpaRepository<DisponibilidadVeterinarioEntity, Long> {
    List<DisponibilidadVeterinarioEntity> findByVeterinarioId(Long veterinarioId);

    List<DisponibilidadVeterinarioEntity> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana);
}
