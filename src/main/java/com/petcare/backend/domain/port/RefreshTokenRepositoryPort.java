package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.RefreshToken;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepositoryPort {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(RefreshToken refreshToken);
    void deleteByUsuarioId(Long usuarioId);
    void deleteByToken(String token);
    void deleteAllExpiredBefore(Instant instant);
}
