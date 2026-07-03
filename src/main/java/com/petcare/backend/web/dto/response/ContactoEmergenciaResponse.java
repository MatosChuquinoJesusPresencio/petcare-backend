package com.petcare.backend.web.dto.response;

public record ContactoEmergenciaResponse(
        Long id,
        Long duenoId,
        String nombre,
        String telefono,
        String relacion
) {}
