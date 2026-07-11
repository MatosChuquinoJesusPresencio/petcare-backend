package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaReprogramarRequest(
        @NotNull(message = "La nueva fecha y hora son obligatorias")
        LocalDateTime dateTime
) {
}
