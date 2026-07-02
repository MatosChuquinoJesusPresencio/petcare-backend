package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.RecetaVeterinariaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaVeterinariaRepository extends JpaRepository<RecetaVeterinariaEntity, Long> {
}
