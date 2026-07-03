package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TriajeRequest(
        @NotNull(message = "Appointment ID is required")
        Long appointmentId,

        @NotBlank(message = "Reason for visit is required")
        String reasonForVisit,

        @NotBlank(message = "Urgency level is required")
        String urgencyLevel,

        String visibleSigns,

        String observations,

        BigDecimal weight,

        BigDecimal temperature,

        Integer heartRate,

        Integer respiratoryRate
) {
}
