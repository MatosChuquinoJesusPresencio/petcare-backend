package com.petcare.backend.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaRequest(
        @NotNull(message = "El ID de la mascota es obligatorio")
        Long mascotaId,

        @NotNull(message = "El ID del veterinario es obligatorio")
        Long veterinarioId,

        @NotNull(message = "El ID del servicio es obligatorio")
        Long servicioId,

        @NotNull(message = "La fecha y hora de la cita son obligatorias")
        @Future(message = "La fecha y hora de la cita deben estar en el futuro")
        LocalDateTime fechaHora,

        String notas
) {
}
