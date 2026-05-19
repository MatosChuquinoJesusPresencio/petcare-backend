package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadVeterinarioRepository extends JpaRepository<DisponibilidadVeterinarioEntity, Long> {
}
