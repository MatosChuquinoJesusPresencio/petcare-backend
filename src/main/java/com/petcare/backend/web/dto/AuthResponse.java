package com.petcare.backend.web.dto;

public record AuthResponse(
        String token,
        String refreshToken,
        String username,
        String rol
) {
}
