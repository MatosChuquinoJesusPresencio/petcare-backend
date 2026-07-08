package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.domain.port.RefreshTokenRepositoryPort;
import com.petcare.backend.persistence.entity.RefreshTokenEntity;
import com.petcare.backend.persistence.mapper.RefreshTokenMapper;
import com.petcare.backend.persistence.repository.RefreshTokenJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository refreshTokenJpaRepository,
                                         RefreshTokenMapper refreshTokenMapper,
                                         UsuarioJpaRepository usuarioJpaRepository) {
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
        this.refreshTokenMapper = refreshTokenMapper;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token).map(refreshTokenMapper::toDomain);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        if (refreshToken.getUsuario() != null && refreshToken.getUsuario().getId() != null) {
            entity.setUsuario(usuarioJpaRepository.getReferenceById(refreshToken.getUsuario().getId()));
        }
        RefreshTokenEntity saved = refreshTokenJpaRepository.save(entity);
        return refreshTokenMapper.toDomain(saved);
    }

    @Override
    public void deleteByUsuarioId(Long usuarioId) {
        refreshTokenJpaRepository.deleteByUsuarioId(usuarioId);
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenJpaRepository.deleteByToken(token);
    }

    @Override
    public void deleteAllExpiredBefore(Instant instant) {
        refreshTokenJpaRepository.deleteAllExpiredBefore(instant);
    }
}
