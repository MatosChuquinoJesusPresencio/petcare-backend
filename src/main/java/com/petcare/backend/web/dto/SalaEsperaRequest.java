package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotNull;

public record SalaEsperaRequest(
        @NotNull(message = "Appointment ID is required")
        Long citaId,

        String observaciones
) {
}
