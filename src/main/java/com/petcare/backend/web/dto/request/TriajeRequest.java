package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TriajeRequest(
        @NotNull(message = "El ID de la cita es obligatorio")
        Long appointmentId,

        @NotBlank(message = "El motivo de la visita es obligatorio")
        String reasonForVisit,

        @NotBlank(message = "El nivel de urgencia es obligatorio")
        String urgencyLevel,

        String visibleSigns,

        String observations,

        BigDecimal weight,

        BigDecimal temperature,

        Integer heartRate,

        Integer respiratoryRate
) {
}
