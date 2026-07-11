package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record SalaEsperaRequest(
        @NotNull(message = "El ID de la cita es obligatorio")
        Long appointmentId,

        String observations
) {
}
