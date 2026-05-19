package com.petcare.backend.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaReprogramarRequest(
        @NotNull(message = "La nueva fecha y hora son obligatorias")
        @Future(message = "La nueva fecha y hora deben estar en el futuro")
        LocalDateTime fechaHora
) {
}
