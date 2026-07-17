package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Notificacion;
import java.util.List;

public interface NotificacionRepositoryPort {
    Notificacion save(Notificacion notificacion);
    List<Notificacion> findByDestinoUsuarioId(Long usuarioId);
    List<Notificacion> findAllByOrderByCreadoEnDesc();
    long countNoLeidasByDestinoUsuarioId(Long usuarioId);
    void marcarComoLeidas(Long usuarioId);
}
