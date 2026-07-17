package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.PlanTratamiento;
import java.util.List;
import java.util.Optional;

public interface PlanTratamientoRepositoryPort {
    Optional<PlanTratamiento> findById(Long id);
    List<PlanTratamiento> findByMascotaId(Long mascotaId);
    List<PlanTratamiento> findByAtencionClinicaId(Long atencionClinicaId);
    PlanTratamiento save(PlanTratamiento plan);
    void deleteById(Long id);
}
