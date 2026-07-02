package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.DuenoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DuenoRepository extends JpaRepository<DuenoEntity, Long>, JpaSpecificationExecutor<DuenoEntity> {
    java.util.Optional<DuenoEntity> findByDni(String dni);
    java.util.Optional<DuenoEntity> findByEmail(String email);
}
