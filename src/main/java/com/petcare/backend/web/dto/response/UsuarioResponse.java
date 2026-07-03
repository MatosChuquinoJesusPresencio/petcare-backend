package com.petcare.backend.web.dto.response;

public record UsuarioResponse(
        Long id,
        String nombres,
        String apellidos,
        String email,
        String telefono,
        String rol,
        Boolean estado
) {}
