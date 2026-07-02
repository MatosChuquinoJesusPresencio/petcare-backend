package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadVeterinarioRepository extends JpaRepository<DisponibilidadVeterinarioEntity, Long> {
    java.util.List<DisponibilidadVeterinarioEntity> findByVeterinarioId(Long veterinarioId);
    java.util.List<DisponibilidadVeterinarioEntity> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana);
}
