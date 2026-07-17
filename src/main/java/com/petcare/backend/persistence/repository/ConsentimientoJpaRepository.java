package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.ConsentimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsentimientoJpaRepository extends JpaRepository<ConsentimientoEntity, Long> {
    List<ConsentimientoEntity> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId);
}
