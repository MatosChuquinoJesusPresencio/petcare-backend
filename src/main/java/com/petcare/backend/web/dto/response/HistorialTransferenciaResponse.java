package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record HistorialTransferenciaResponse(
        Long id,
        Long petId,
        Long previousOwnerId,
        Long newOwnerId,
        Instant date,
        String reason,
        Long responsibleUserId
) {}
