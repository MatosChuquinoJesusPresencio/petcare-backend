package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadVeterinarioRepositoryPort {
    List<DisponibilidadVeterinario> findByVeterinarioId(Long veterinarioId);
    List<DisponibilidadVeterinario> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana);
    Optional<DisponibilidadVeterinario> findById(Long id);
    DisponibilidadVeterinario save(DisponibilidadVeterinario disponibilidad);
}
