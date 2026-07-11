package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    void deleteByUsuarioId(Long usuarioId);

    @Modifying
    @Transactional
    void deleteByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.usuario.id = :usuarioId AND r.fechaExpiracion < :instant")
    void deleteExpiredByUsuarioId(@Param("usuarioId") Long usuarioId, @Param("instant") Instant instant);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.fechaExpiracion < :instant")
    void deleteAllExpiredBefore(@Param("instant") Instant instant);
}
