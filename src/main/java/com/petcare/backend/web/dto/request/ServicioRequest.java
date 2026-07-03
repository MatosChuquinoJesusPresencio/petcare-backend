package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ServicioRequest(
        @NotBlank(message = "Service name is required")
        String name,

        String description,

        @NotNull(message = "Duration is required")
        @Min(value = 5, message = "Minimum duration is 5 minutes")
        Integer durationMinutes,

        @NotNull(message = "Referential cost is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Cost cannot be negative")
        BigDecimal referentialCost
) {
}
