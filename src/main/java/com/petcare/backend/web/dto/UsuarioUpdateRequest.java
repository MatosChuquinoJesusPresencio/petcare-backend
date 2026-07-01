package com.petcare.backend.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        String phone,

        @NotBlank(message = "Role is required")
        @Pattern(
                regexp = "(?i)^(ADMINISTRADOR|ASISTENTE|VETERINARIO|DUENO)$",
                message = "Role must be ADMINISTRADOR, ASISTENTE, VETERINARIO, or DUENO")
        String role
) {
}
