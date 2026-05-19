package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.TriajeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriajeRepository extends JpaRepository<TriajeEntity, Long> {
}
