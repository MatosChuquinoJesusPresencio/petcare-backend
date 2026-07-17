package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record SeguimientoRequest(
        @NotNull Long veterinarioId,
        @NotBlank String tipo,
        @NotNull LocalDateTime fechaProgramada,
        @NotBlank String motivo,
        Long duenoNotificadoId
) {}
