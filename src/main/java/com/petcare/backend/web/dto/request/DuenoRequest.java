package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DuenoRequest(
        @NotBlank(message = "El DNI es obligatorio")
        @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 caracteres")
        String dni,

        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe ser exactamente 9 dígitos")
        String phone,

        String address,

        Long userId,

        String firstName,

        String lastName,

        @Email(message = "El correo electrónico debe ser válido")
        String email,

        String password
) {
}
