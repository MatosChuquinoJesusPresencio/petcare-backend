package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record PlanTratamientoRequest(
        @NotBlank(message = "El título es obligatorio")
        String titulo,

        String descripcion,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        LocalDate fechaFinEstimada,

        @NotNull(message = "El ID del veterinario es obligatorio")
        Long veterinarioId,

        List<ActividadRequest> actividades
) {}
