package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Notificacion;
import com.petcare.backend.domain.port.NotificacionRepositoryPort;
import com.petcare.backend.domain.port.NotificadorPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepositoryPort notificacionRepositoryPort;
    private final NotificadorPort notificadorPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public NotificacionService(NotificacionRepositoryPort notificacionRepositoryPort,
                                NotificadorPort notificadorPort,
                                UsuarioRepositoryPort usuarioRepositoryPort) {
        this.notificacionRepositoryPort = notificacionRepositoryPort;
        this.notificadorPort = notificadorPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public Notificacion enviar(String tipo, Long destinoUsuarioId, Long mascotaId,
                                Long citaId, String canal, String mensaje) {
        if ("SMS".equals(canal) || "WHATSAPP".equals(canal)) {
            var usuario = usuarioRepositoryPort.findById(destinoUsuarioId).orElse(null);
            String telefono = usuario != null ? usuario.getTelefono() : null;
            if (telefono == null || telefono.isBlank()) {
                log.warn("[Notificacion] Usuario {} no tiene telefono registrado. Notificacion {} no enviada.", destinoUsuarioId, canal);
                return registrarError(tipo, destinoUsuarioId, canal, mensaje, "Telefono no disponible");
            }
            boolean enviado = "SMS".equals(canal)
                    ? notificadorPort.enviarSMS(telefono, mensaje)
                    : notificadorPort.enviarWhatsApp(telefono, mensaje);
            if (enviado) {
                return registrar(tipo, destinoUsuarioId, mascotaId, citaId, canal, mensaje);
            } else {
                return registrarError(tipo, destinoUsuarioId, canal, mensaje, "Error al enviar " + canal);
            }
        }
        return registrar(tipo, destinoUsuarioId, mascotaId, citaId, canal, mensaje);
    }

    public Notificacion registrar(String tipo, Long destinoUsuarioId, Long mascotaId,
                                  Long citaId, String canal, String mensaje) {
        Notificacion n = new Notificacion();
        n.setTipo(tipo);
        n.setDestinoUsuarioId(destinoUsuarioId);
        n.setMascotaId(mascotaId);
        n.setCitaId(citaId);
        n.setCanal(canal);
        n.setMensaje(mensaje);
        n.setEstado("ENVIADO");
        n.setFechaEnvio(Instant.now());
        n.setLeido(false);
        return notificacionRepositoryPort.save(n);
    }

    public Notificacion registrarError(String tipo, Long destinoUsuarioId, String canal,
                                        String mensaje, String errorMensaje) {
        Notificacion n = new Notificacion();
        n.setTipo(tipo);
        n.setDestinoUsuarioId(destinoUsuarioId);
        n.setCanal(canal);
        n.setMensaje(mensaje);
        n.setEstado("ERROR");
        n.setErrorMensaje(errorMensaje);
        return notificacionRepositoryPort.save(n);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarPorUsuario(Long usuarioId) {
        return notificacionRepositoryPort.findByDestinoUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarTodas() {
        return notificacionRepositoryPort.findAllByOrderByCreadoEnDesc();
    }

    @Transactional(readOnly = true)
    public long contarNoLeidas(Long usuarioId) {
        return notificacionRepositoryPort.countNoLeidasByDestinoUsuarioId(usuarioId);
    }

    @Transactional
    public void marcarComoLeidas(Long usuarioId) {
        notificacionRepositoryPort.marcarComoLeidas(usuarioId);
    }
}
