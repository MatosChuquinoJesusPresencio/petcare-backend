package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.HistorialTransferenciaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialTransferenciaRepository extends JpaRepository<HistorialTransferenciaEntity, Long> {
    Page<HistorialTransferenciaEntity> findByMascotaIdOrderByFechaDesc(Long mascotaId, Pageable pageable);
}
