package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.TriajeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TriajeRepository extends JpaRepository<TriajeEntity, Long> {
    Optional<TriajeEntity> findByCitaId(Long citaId);
    Page<TriajeEntity> findByNivelUrgencia(String nivelUrgencia, Pageable pageable);
}
