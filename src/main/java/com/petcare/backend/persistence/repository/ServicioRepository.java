package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.ServicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<ServicioEntity, Long> {
    java.util.List<ServicioEntity> findByActivo(Boolean activo);
}
