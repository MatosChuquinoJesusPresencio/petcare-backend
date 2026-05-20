package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.domain.port.RefreshTokenRepositoryPort;
import com.petcare.backend.persistence.entity.RefreshTokenEntity;
import com.petcare.backend.persistence.mapper.RefreshTokenMapper;
import com.petcare.backend.persistence.repository.RefreshTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    public RefreshTokenRepositoryAdapter(RefreshTokenRepository refreshTokenRepository, RefreshTokenMapper refreshTokenMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(refreshTokenMapper::toModel);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        RefreshTokenEntity saved = refreshTokenRepository.save(entity);
        return refreshTokenMapper.toModel(saved);
    }

    @Override
    @Transactional
    public void deleteByUsuarioId(Long usuarioId) {
        refreshTokenRepository.deleteByUsuarioId(usuarioId);
    }
}
