package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.AtencionClinicaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtencionClinicaRepository extends JpaRepository<AtencionClinicaEntity, Long> {
    Optional<AtencionClinicaEntity> findByCitaId(Long citaId);
    Page<AtencionClinicaEntity> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId, Pageable pageable);
}
