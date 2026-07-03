package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CitaReprogramarRequest(
        @NotNull(message = "New date and time are required")
        LocalDateTime dateTime
) {
}
