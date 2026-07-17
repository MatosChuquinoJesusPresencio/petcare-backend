package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record HistorialVacunacionRequest(
        @NotBlank(message = "El tipo es obligatorio")
        String tipo,

        @NotBlank(message = "El nombre del producto es obligatorio")
        String nombreProducto,

        @NotNull(message = "La fecha de aplicación es obligatoria")
        LocalDate fechaAplicacion,

        LocalDate proximaDosis,

        String lote,

        String fabricante,

        String dosis,

        String viaAdministracion,

        @NotNull(message = "El ID del veterinario es obligatorio")
        Long veterinarioId,

        String observaciones
) {}
