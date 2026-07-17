package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Seguimiento;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SeguimientoRepositoryPort {
    Optional<Seguimiento> findById(Long id);
    List<Seguimiento> findByAtencionClinicaId(Long atencionClinicaId);
    List<Seguimiento> findByMascotaId(Long mascotaId);
    List<Seguimiento> findByFechaProgramadaBetween(Instant desde, Instant hasta);
    List<Seguimiento> findAll();
    Seguimiento save(Seguimiento seguimiento);
}
