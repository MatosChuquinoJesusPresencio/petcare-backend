package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.HistorialTransferenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialTransferenciaRepository extends JpaRepository<HistorialTransferenciaEntity, Long> {
}
