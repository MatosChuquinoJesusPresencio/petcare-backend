package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

public record ActividadRequest(
        @NotBlank(message = "El tipo es obligatorio")
        String tipo,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        LocalDate fechaProgramada,

        LocalTime horaProgramada,

        String frecuencia,

        String responsable,

        String observaciones
) {}
