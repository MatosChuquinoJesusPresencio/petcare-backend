package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record AtencionClinicaResponse(
        Long id,
        Long citaId,
        Long mascotaId,
        Long veterinarioId,
        Long triajeId,
        String motivoConsulta,
        String sintomas,
        String diagnostico,
        String tratamiento,
        String observacionesClinicas,
        Long creadoPorId,
        Instant creadoEn,
        Long actualizadoPorId,
        Instant actualizadoEn
) {}
