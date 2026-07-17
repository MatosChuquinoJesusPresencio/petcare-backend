package com.petcare.backend.web.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PlanTratamientoResponse(
        Long id,
        Long mascotaId,
        String mascotaNombre,
        Long atencionClinicaId,
        Long veterinarioId,
        String veterinarioNombre,
        String titulo,
        String descripcion,
        LocalDate fechaInicio,
        LocalDate fechaFinEstimada,
        String estado,
        Long createdBy,
        Instant createdAt,
        List<ActividadResponse> actividades
) {}
