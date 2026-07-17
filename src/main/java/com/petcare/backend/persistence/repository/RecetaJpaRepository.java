package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.RecetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaJpaRepository extends JpaRepository<RecetaEntity, Long> {
    List<RecetaEntity> findByAtencionClinicaIdOrderByCreadoEnDesc(Long atencionClinicaId);
    List<RecetaEntity> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId);
}
