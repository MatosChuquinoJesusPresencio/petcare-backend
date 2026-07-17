package com.petcare.backend.web.dto.response;

import java.time.Instant;
import java.util.List;

public record RecetaResponse(
        Long id,
        Long atencionClinicaId,
        Long mascotaId,
        String mascotaNombre,
        Long veterinarioId,
        String veterinarioNombre,
        String diagnostico,
        String notasAdicionales,
        String estado,
        Long createdBy,
        Instant createdAt,
        List<RecetaDetalleResponse> detalles
) {}
