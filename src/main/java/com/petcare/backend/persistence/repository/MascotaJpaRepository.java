package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.MascotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MascotaJpaRepository extends JpaRepository<MascotaEntity, Long>, JpaSpecificationExecutor<MascotaEntity> {
    Optional<MascotaEntity> findByMicrochip(String microchip);
}
