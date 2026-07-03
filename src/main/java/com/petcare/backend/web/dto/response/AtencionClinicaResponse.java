package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record AtencionClinicaResponse(
        Long id,
        Long appointmentId,
        Long petId,
        Long veterinarianId,
        Long triageId,
        String reasonForConsultation,
        String symptoms,
        String diagnosis,
        String treatment,
        String clinicalObservations,
        Long createdBy,
        Instant createdAt,
        Long updatedBy,
        Instant updatedAt
) {}
