package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record CitaResponse(
        Long id,
        Long mascotaId,
        Long veterinarioId,
        Long servicioId,
        Instant fechaHora,
        String estado,
        String notas,
        Long creadoPorId,
        Instant creadoEn,
        Long actualizadoPorId,
        Instant actualizadoEn
) {}
