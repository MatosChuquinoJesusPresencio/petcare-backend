package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ContactoEmergenciaRequest(
        @NotBlank(message = "Contact name is required")
        String name,

        @NotBlank(message = "Contact phone is required")
        String phone,

        String relation
) {
}
