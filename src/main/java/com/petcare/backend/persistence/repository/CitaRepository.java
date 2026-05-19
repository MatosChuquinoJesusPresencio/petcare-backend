package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.CitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {
}
