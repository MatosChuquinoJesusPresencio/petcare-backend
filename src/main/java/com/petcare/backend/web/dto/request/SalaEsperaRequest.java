package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record SalaEsperaRequest(
        @NotNull(message = "Appointment ID is required")
        Long appointmentId,

        String observations
) {
}
