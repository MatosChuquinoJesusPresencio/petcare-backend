package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtencionClinicaRequest(
        @NotNull(message = "Appointment ID is required")
        Long citaId,

        @NotBlank(message = "Reason for consultation is required")
        String motivoConsulta,

        String sintomas,

        @NotBlank(message = "Diagnosis is required")
        String diagnostico,

        String observacionesClinicas,

        Long triajeId
) {
}
