package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.HistorialVacunacion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistorialVacunacionRepositoryPort {
    Optional<HistorialVacunacion> findById(Long id);
    List<HistorialVacunacion> findByMascotaId(Long mascotaId);
    List<HistorialVacunacion> findByProximaDosisBetween(LocalDate desde, LocalDate hasta);
    HistorialVacunacion save(HistorialVacunacion vacunacion);
    void deleteById(Long id);
}
