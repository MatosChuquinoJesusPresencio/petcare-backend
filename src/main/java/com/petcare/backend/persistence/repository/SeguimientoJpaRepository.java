package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.SeguimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface SeguimientoJpaRepository extends JpaRepository<SeguimientoEntity, Long> {
    List<SeguimientoEntity> findByAtencionClinicaId(Long atencionClinicaId);
    List<SeguimientoEntity> findByMascotaId(Long mascotaId);
    List<SeguimientoEntity> findByFechaProgramadaBetween(Instant desde, Instant hasta);
}
