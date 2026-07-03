package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.domain.port.RefreshTokenRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.PetcareException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs:604800000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public RefreshTokenService(RefreshTokenRepositoryPort refreshTokenRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort) {
        this.refreshTokenRepositoryPort = refreshTokenRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public RefreshToken createRefreshToken(Long userId) {
        refreshTokenRepositoryPort.deleteByUsuarioId(userId);

        RefreshToken refreshToken = new RefreshToken(
                null,
                usuarioRepositoryPort.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")),
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        return refreshTokenRepositoryPort.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepositoryPort.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getFechaExpiracion().isBefore(Instant.now())) {
            refreshTokenRepositoryPort.deleteByUsuarioId(token.getUsuario().getId());
            throw new PetcareException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepositoryPort.deleteByUsuarioId(userId);
    }
}
