package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransferenciaRequest(
        @NotNull(message = "New owner ID is required")
        Long nuevoDuenoId,

        @NotBlank(message = "Reason is required")
        String motivo
) {
}
