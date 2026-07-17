package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Consentimiento;
import java.util.List;
import java.util.Optional;

public interface ConsentimientoRepositoryPort {
    Optional<Consentimiento> findById(Long id);
    List<Consentimiento> findByMascotaId(Long mascotaId);
    List<Consentimiento> findAll();
    Consentimiento save(Consentimiento consentimiento);
}
