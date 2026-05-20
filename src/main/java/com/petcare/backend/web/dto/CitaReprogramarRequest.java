package com.petcare.backend.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaReprogramarRequest(
        @NotNull(message = "New date and time are required")
        @Future(message = "New date and time must be in the future")
        LocalDateTime dateTime
) {
}
