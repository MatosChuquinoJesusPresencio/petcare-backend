package com.petcare.backend.web.dto.response;

public record DuenoResponse(
        Long id,
        String dni,
        String phone,
        String address,
        UsuarioResponse usuario
) {}
