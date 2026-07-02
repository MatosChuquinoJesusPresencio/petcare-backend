package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BloqueoVeterinarioRepositoryPort {
    List<BloqueoVeterinario> findByVeterinarioId(Long veterinarioId);
    List<BloqueoVeterinario> findByVeterinarioIdAndFecha(Long veterinarioId, Instant fecha);
    Optional<BloqueoVeterinario> findById(Long id);
    BloqueoVeterinario save(BloqueoVeterinario bloqueo);
    void deleteById(Long id);
}
