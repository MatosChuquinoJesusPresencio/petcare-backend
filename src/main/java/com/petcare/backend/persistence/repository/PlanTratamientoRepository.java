package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.PlanTratamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanTratamientoRepository extends JpaRepository<PlanTratamientoEntity, Long> {
}
