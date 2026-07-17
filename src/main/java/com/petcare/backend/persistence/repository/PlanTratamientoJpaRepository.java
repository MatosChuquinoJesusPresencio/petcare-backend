package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.PlanTratamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanTratamientoJpaRepository extends JpaRepository<PlanTratamientoEntity, Long> {
    List<PlanTratamientoEntity> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId);
    List<PlanTratamientoEntity> findByAtencionClinicaId(Long atencionClinicaId);
}
