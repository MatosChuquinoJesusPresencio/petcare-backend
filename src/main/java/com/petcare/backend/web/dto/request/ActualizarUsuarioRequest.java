package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ActualizarUsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico debe ser válido")
        String email,

        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe ser exactamente 9 dígitos")
        String phone,

        @NotBlank(message = "El rol es obligatorio")
        String role
) {}
