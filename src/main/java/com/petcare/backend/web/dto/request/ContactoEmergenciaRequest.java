package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContactoEmergenciaRequest(
        @NotBlank(message = "El nombre del contacto es obligatorio")
        String name,

        @NotBlank(message = "El teléfono del contacto es obligatorio")
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe ser exactamente 9 dígitos")
        String phone,

        String relation
) {
}
