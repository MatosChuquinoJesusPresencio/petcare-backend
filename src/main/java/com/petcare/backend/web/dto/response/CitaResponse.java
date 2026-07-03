package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record CitaResponse(
        Long id,
        Long petId,
        Long veterinarianId,
        Long serviceId,
        Instant dateTime,
        String status,
        String notes,
        Long createdBy,
        Instant createdAt,
        Long updatedBy,
        Instant updatedAt
) {}
