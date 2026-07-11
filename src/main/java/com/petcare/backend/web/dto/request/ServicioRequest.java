package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ServicioRequest(
        @NotBlank(message = "El nombre del servicio es obligatorio")
        String name,

        String description,

        @NotNull(message = "La duración es obligatoria")
        @Min(value = 5, message = "La duración mínima es 5 minutos")
        Integer durationMinutes,

        @NotNull(message = "El costo referencial es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El costo no puede ser negativo")
        BigDecimal referentialCost
) {
}
