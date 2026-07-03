package com.petcare.backend.web.dto.response;

public record DuenoResponse(
        Long id,
        String dni,
        String telefono,
        String direccion,
        Long usuarioId
) {}
