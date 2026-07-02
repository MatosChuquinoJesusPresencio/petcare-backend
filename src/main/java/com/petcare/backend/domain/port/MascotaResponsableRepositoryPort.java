package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.MascotaResponsable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface MascotaResponsableRepositoryPort {
    List<MascotaResponsable> findByMascotaId(Long mascotaId);
    Page<MascotaResponsable> findByDuenoId(Long duenoId, Pageable pageable);
    Optional<MascotaResponsable> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
    MascotaResponsable save(MascotaResponsable mascotaResponsable);
    void deleteByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
}
