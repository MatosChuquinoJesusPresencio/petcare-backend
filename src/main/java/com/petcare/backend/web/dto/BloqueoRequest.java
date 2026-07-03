package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueoRequest(
        @NotNull(message = "Veterinarian ID is required")
        Long veterinarianId,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        LocalTime endTime,

        String reason
) {
}
