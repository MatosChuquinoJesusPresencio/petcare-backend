package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.VacunacionDesparasitacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacunacionDesparasitacionRepository extends JpaRepository<VacunacionDesparasitacionEntity, Long> {
}
