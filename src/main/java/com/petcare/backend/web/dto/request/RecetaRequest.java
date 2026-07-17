package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RecetaRequest(
        @NotNull(message = "El diagnóstico es obligatorio")
        String diagnostico,

        String notasAdicionales,

        @NotNull(message = "El ID del veterinario es obligatorio")
        Long veterinarioId,

        List<RecetaDetalleRequest> detalles
) {}
