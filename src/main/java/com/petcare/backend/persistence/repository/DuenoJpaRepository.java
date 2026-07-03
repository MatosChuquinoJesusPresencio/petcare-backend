package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.DuenoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DuenoJpaRepository extends JpaRepository<DuenoEntity, Long>, JpaSpecificationExecutor<DuenoEntity> {
    Optional<DuenoEntity> findByDni(String dni);

    Optional<DuenoEntity> findByUsuarioEmail(String email);
}
