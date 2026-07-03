package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DuenoRequest(
        @NotBlank(message = "DNI is required")
        @Size(min = 8, max = 20, message = "DNI must be between 8 and 20 characters")
        String dni,

        String phone,

        String address,

        Long userId,

        String firstName,

        String lastName,

        String email,

        String password
) {
}
