package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtencionClinicaRequest(
        @NotNull(message = "Appointment ID is required")
        Long appointmentId,

        @NotBlank(message = "Reason for consultation is required")
        String reasonForConsultation,

        String symptoms,

        @NotBlank(message = "Diagnosis is required")
        String diagnosis,

        String clinicalObservations,

        Long triageId
) {
}
