package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CitaEstadoRequest(
        @NotBlank(message = "El nuevo estado es obligatorio")
        String estado
) {
}
