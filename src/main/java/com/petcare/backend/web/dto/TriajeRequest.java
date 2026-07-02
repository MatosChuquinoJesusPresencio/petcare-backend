package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TriajeRequest(
        @NotNull(message = "Appointment ID is required")
        Long citaId,

        @NotBlank(message = "Reason for visit is required")
        String motivoVisita,

        @NotBlank(message = "Urgency level is required")
        String nivelUrgencia,

        String signosVisibles,

        String observaciones,

        BigDecimal peso,

        BigDecimal temperatura,

        Integer frecuenciaCardiaca,

        Integer frecuenciaRespiratoria
) {
}
