package com.petcare.backend.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record DisponibilidadRequest(
        @NotNull(message = "Veterinarian ID is required")
        Long veterinarioId,

        @NotNull(message = "Day of week is required")
        @Min(value = 1, message = "Day of week must be between 1 (Monday) and 7 (Sunday)")
        @Max(value = 7, message = "Day of week must be between 1 (Monday) and 7 (Sunday)")
        Integer diaSemana,

        @NotNull(message = "Start time is required")
        LocalTime horaInicio,

        @NotNull(message = "End time is required")
        LocalTime horaFin
) {
}
