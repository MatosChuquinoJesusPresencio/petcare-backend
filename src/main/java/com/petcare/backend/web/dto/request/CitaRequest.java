package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaRequest(
        @NotNull(message = "El ID de la mascota es obligatorio")
        Long petId,

        @NotNull(message = "El ID del veterinario es obligatorio")
        Long veterinarianId,

        @NotNull(message = "El ID del servicio es obligatorio")
        Long serviceId,

        @NotNull(message = "La fecha y hora son obligatorias")
        LocalDateTime dateTime,

        String notes
) {
}
