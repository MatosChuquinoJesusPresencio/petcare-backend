package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record SalaEsperaResponse(
        Long id,
        Long citaId,
        Long mascotaId,
        Instant fechaLlegada,
        String estado,
        String observaciones
) {}
