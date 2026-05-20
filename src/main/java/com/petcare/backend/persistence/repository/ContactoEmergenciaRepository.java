package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoEmergenciaRepository extends JpaRepository<ContactoEmergenciaEntity, Long> {
    Page<ContactoEmergenciaEntity> findByDuenoId(Long duenoId, Pageable pageable);
}
