package com.petcare.backend.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record TriajeResponse(
        Long id,
        Long appointmentId,
        String reasonForVisit,
        String urgencyLevel,
        String visibleSigns,
        String observations,
        BigDecimal weight,
        BigDecimal temperature,
        Integer heartRate,
        Integer respiratoryRate,
        Long assistantId,
        Instant createdAt,
        Instant updatedAt
) {}
