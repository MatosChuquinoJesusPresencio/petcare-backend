package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record ConsentimientoResponse(
        Long id,
        Long mascotaId,
        String mascotaNombre,
        Long duenoId,
        Long atencionClinicaId,
        Long veterinarioId,
        String veterinarioNombre,
        String tipoProcedimiento,
        String descripcionProcedimiento,
        String riesgosDescritos,
        String alternativas,
        Boolean consentido,
        Instant fechaConsentimiento,
        String duenoNombreVerificado,
        String testigoNombre,
        String observaciones,
        Instant createdAt
) {}
