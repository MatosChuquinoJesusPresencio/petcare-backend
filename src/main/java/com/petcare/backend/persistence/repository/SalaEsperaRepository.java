package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.SalaEsperaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaEsperaRepository extends JpaRepository<SalaEsperaEntity, Long> {
}
