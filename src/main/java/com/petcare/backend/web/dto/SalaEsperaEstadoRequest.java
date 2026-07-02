package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;

public record SalaEsperaEstadoRequest(
        @NotBlank(message = "Status is required")
        String estado
) {
}
