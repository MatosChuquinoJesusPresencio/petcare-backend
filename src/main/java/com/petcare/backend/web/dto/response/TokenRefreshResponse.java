package com.petcare.backend.web.dto.response;

public record TokenRefreshResponse(
        String accessToken
) {
    public TokenRefreshResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
