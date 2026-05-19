package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaResponsableRepository extends JpaRepository<MascotaResponsableEntity, Long> {
}
