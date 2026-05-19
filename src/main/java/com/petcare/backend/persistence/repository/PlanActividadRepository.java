package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.PlanActividadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanActividadRepository extends JpaRepository<PlanActividadEntity, Long> {
}
