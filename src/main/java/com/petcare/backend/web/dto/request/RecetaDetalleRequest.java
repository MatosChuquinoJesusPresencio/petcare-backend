package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RecetaDetalleRequest(
        @NotBlank(message = "El medicamento es obligatorio")
        String medicamento,

        String presentacion,

        @NotBlank(message = "La dosis es obligatoria")
        String dosis,

        @NotBlank(message = "La frecuencia es obligatoria")
        String frecuencia,

        @NotBlank(message = "La duración es obligatoria")
        String duracion,

        String viaAdministracion,

        String indicaciones
) {}
