package com.petcare.backend.web.dto.response;

public record UsuarioResponse(
        Long id,
        String names,
        String lastNames,
        String email,
        String phone,
        String rol,
        Boolean active
) {}
