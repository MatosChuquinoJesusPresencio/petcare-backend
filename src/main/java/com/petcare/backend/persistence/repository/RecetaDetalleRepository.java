package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.RecetaDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaDetalleRepository extends JpaRepository<RecetaDetalleEntity, Long> {
}
