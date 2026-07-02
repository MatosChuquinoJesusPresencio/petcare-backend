package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueoRequest(
        @NotNull(message = "Veterinarian ID is required")
        Long veterinarioId,

        @NotNull(message = "Date is required")
        LocalDate fecha,

        @NotNull(message = "Start time is required")
        LocalTime horaInicio,

        @NotNull(message = "End time is required")
        LocalTime horaFin,

        String motivo
) {
}
