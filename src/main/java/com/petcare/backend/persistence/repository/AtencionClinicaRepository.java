package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.AtencionClinicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtencionClinicaRepository extends JpaRepository<AtencionClinicaEntity, Long> {
}
