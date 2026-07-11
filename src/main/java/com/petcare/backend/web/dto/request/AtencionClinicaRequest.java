package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtencionClinicaRequest(
        @NotNull(message = "El ID de la cita es obligatorio")
        Long appointmentId,

        @NotBlank(message = "El motivo de consulta es obligatorio")
        String reasonForConsultation,

        String symptoms,

        @NotBlank(message = "El diagnóstico es obligatorio")
        String diagnosis,

        String clinicalObservations,

        String treatment,

        @NotNull(message = "El ID del triaje es obligatorio")
        Long triageId
) {
}
