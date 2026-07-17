package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record SeguimientoResponse(
        Long id,
        Long atencionClinicaId,
        Long mascotaId,
        String mascotaNombre,
        Long veterinarioId,
        String veterinarioNombre,
        Long duenoNotificadoId,
        String tipo,
        Instant fechaProgramada,
        Instant fechaCompletada,
        String motivo,
        String resultado,
        String estado,
        Instant createdAt
) {}
