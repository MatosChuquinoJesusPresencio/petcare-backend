package com.petcare.backend.web.dto.response;

public record AuthResponse(
        Long id,
        String username,
        String role
) {
}
