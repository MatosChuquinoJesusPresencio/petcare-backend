package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadVeterinarioJpaRepository extends JpaRepository<DisponibilidadVeterinarioEntity, Long> {
    @EntityGraph(attributePaths = {"veterinario"})
    List<DisponibilidadVeterinarioEntity> findByVeterinarioId(Long veterinarioId);

    @EntityGraph(attributePaths = {"veterinario"})
    List<DisponibilidadVeterinarioEntity> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana);
}
