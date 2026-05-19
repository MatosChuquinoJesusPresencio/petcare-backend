package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.AuditoriaClinicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaClinicaRepository extends JpaRepository<AuditoriaClinicaEntity, Long> {
}
