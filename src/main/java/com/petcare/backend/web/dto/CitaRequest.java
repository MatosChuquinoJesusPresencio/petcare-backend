package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaRequest(
        @NotNull(message = "Pet ID is required")
        Long petId,

        @NotNull(message = "Veterinarian ID is required")
        Long veterinarianId,

        @NotNull(message = "Service ID is required")
        Long serviceId,

        @NotNull(message = "Date and time are required")
        LocalDateTime dateTime,

        String notes
) {
}
