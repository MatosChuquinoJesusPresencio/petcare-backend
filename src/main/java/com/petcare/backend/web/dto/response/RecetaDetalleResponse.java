package com.petcare.backend.web.dto.response;

public record RecetaDetalleResponse(
        Long id,
        String medicamento,
        String presentacion,
        String dosis,
        String frecuencia,
        String duracion,
        String viaAdministracion,
        String indicaciones
) {}
