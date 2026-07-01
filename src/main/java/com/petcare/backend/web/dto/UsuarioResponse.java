package com.petcare.backend.web.dto;

public record UsuarioResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        String role,
        Boolean active
) {
}
