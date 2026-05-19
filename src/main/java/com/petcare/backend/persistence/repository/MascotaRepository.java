package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.MascotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepository extends JpaRepository<MascotaEntity, Long> {
    java.util.Optional<MascotaEntity> findByMicrochip(String microchip);
}
