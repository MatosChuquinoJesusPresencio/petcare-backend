package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaResponsableRepository extends JpaRepository<MascotaResponsableEntity, Long> {
    java.util.List<MascotaResponsableEntity> findByMascotaId(Long mascotaId);
    Page<MascotaResponsableEntity> findByDuenoId(Long duenoId, Pageable pageable);
    java.util.Optional<MascotaResponsableEntity> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
    void deleteByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
}
