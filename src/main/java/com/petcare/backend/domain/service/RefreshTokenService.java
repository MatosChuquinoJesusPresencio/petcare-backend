package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.domain.port.RefreshTokenPort;
import com.petcare.backend.domain.port.UsuarioPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs:604800000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenPort refreshTokenPort;
    private final UsuarioPort usuarioPort;

    public RefreshTokenService(RefreshTokenPort refreshTokenPort, UsuarioPort usuarioPort) {
        this.refreshTokenPort = refreshTokenPort;
        this.usuarioPort = usuarioPort;
    }

    public RefreshToken createRefreshToken(Long userId) {
        refreshTokenPort.deleteByUsuarioId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .usuario(usuarioPort.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")))
                .token(UUID.randomUUID().toString())
                .fechaExpiracion(LocalDateTime.now().plusNanos(refreshTokenDurationMs * 1000000))
                .build();

        return refreshTokenPort.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenPort.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            refreshTokenPort.deleteByUsuarioId(token.getUsuario().getId());
            throw new TokenRefreshException("El Refresh token ha expirado. Por favor vuelva a iniciar sesión.");
        }
        return token;
    }

    public void deleteByUserId(Long userId) {
        refreshTokenPort.deleteByUsuarioId(userId);
    }
}
