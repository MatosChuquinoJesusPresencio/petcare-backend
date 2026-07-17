package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionJpaRepository extends JpaRepository<NotificacionEntity, Long> {
    List<NotificacionEntity> findByDestinoUsuarioIdOrderByCreadoEnDesc(Long usuarioId);
    List<NotificacionEntity> findAllByOrderByCreadoEnDesc();

    long countByDestinoUsuarioIdAndLeidoFalse(Long usuarioId);

    @Modifying
    @Query("UPDATE NotificacionEntity n SET n.leido = true WHERE n.destinoUsuarioId = :usuarioId AND n.leido = false")
    void marcarComoLeidas(@Param("usuarioId") Long usuarioId);
}
