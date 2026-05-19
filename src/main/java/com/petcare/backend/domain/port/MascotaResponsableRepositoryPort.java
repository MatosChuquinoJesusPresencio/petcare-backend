package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.MascotaResponsable;
import java.util.List;
import java.util.Optional;

public interface MascotaResponsableRepositoryPort {
    List<MascotaResponsable> findByMascotaId(Long mascotaId);
    List<MascotaResponsable> findByDuenoId(Long duenoId);
    Optional<MascotaResponsable> findByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
    MascotaResponsable save(MascotaResponsable mascotaResponsable);
    void deleteByMascotaIdAndDuenoId(Long mascotaId, Long duenoId);
}
