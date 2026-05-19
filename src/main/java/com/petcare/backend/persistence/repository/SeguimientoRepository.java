package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.SeguimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeguimientoRepository extends JpaRepository<SeguimientoEntity, Long> {
}
