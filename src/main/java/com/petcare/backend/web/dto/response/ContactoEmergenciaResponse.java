package com.petcare.backend.web.dto.response;

public record ContactoEmergenciaResponse(
        Long id,
        Long ownerId,
        String name,
        String phone,
        String relation
) {}
