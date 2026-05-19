package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ContactoEmergenciaRequest(
        @NotBlank(message = "El nombre del contacto es obligatorio")
        String nombre,

        @NotBlank(message = "El teléfono del contacto es obligatorio")
        String telefono,

        String relacion
) {
}
