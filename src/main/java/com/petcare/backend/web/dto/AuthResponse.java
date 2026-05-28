package com.petcare.backend.web.dto;

public record AuthResponse(
        Long id,
        String token,
        String refreshToken,
        String username,
        String role
) {
}
