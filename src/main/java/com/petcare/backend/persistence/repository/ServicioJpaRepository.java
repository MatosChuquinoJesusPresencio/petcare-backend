package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.ServicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServicioJpaRepository extends JpaRepository<ServicioEntity, Long>, JpaSpecificationExecutor<ServicioEntity> {
}
