package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.ConsentimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentimientoRepository extends JpaRepository<ConsentimientoEntity, Long> {
}
