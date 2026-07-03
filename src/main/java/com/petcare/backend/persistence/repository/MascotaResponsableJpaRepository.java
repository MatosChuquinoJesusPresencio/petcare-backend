package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MascotaResponsableJpaRepository extends JpaRepository<MascotaResponsableEntity, Long> {
    List<MascotaResponsableEntity> findByMascotaId(Long mascotaId);

    Page<MascotaResponsableEntity> findByDuenoId(Long duenoId, Pageable pageable);

    Optional<MascotaResponsableEntity> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
}
