package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record SalaEsperaResponse(
        Long id,
        Long appointmentId,
        Long petId,
        Instant arrivalDate,
        String status,
        String observations
) {}
