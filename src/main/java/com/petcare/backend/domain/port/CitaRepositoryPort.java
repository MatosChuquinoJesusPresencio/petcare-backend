package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CitaRepositoryPort {
    Optional<Cita> findById(Long id);
    Page<Cita> findAll(Pageable pageable);
    Page<Cita> findAll(Long mascotaId, Long veterinarioId, Long servicioId, String estado, Instant fechaDesde, Instant fechaHasta, Pageable pageable);
    Page<Cita> findByMascotaId(Long mascotaId, Pageable pageable);
    Page<Cita> findByVeterinarioId(Long veterinarioId, Pageable pageable);
    List<Cita> findByVeterinarioIdAndFechaHoraBetween(Long veterinarioId, Instant start, Instant end);
    Cita save(Cita cita);
}
