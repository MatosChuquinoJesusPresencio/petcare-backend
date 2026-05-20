package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CitaEstadoRequest(
        @NotBlank(message = "New status is required")
        String status
) {
}
