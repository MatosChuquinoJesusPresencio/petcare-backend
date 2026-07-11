package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record DisponibilidadRequest(
        @NotNull(message = "El ID del veterinario es obligatorio")
        Long veterinarianId,

        @NotNull(message = "El día de la semana es obligatorio")
        @Min(value = 1, message = "El día de la semana debe estar entre 1 (Lunes) y 7 (Domingo)")
        @Max(value = 7, message = "El día de la semana debe estar entre 1 (Lunes) y 7 (Domingo)")
        Integer dayOfWeek,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime endTime
) {
}
