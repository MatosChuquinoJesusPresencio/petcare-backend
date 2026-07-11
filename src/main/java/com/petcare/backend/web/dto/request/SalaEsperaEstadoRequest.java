package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SalaEsperaEstadoRequest(
        @NotBlank(message = "El estado es obligatorio")
        String status
) {
}
