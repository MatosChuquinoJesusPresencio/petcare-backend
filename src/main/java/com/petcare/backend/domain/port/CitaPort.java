package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Cita;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaPort {
    Optional<Cita> findById(Long id);
    List<Cita> findAll();
    List<Cita> findByMascotaId(Long mascotaId);
    List<Cita> findByVeterinarioId(Long veterinarioId);
    List<Cita> findByVeterinarioIdAndFechaHoraBetween(Long veterinarioId, LocalDateTime start, LocalDateTime end);
    Cita save(Cita cita);
}
