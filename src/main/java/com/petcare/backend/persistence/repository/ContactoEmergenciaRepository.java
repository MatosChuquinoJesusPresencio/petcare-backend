package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoEmergenciaRepository extends JpaRepository<ContactoEmergenciaEntity, Long>, JpaSpecificationExecutor<ContactoEmergenciaEntity> {
}
