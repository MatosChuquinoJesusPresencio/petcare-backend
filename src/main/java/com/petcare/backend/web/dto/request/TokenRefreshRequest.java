package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank(message = "El token de actualización es obligatorio")
        String refreshToken
) {
}
