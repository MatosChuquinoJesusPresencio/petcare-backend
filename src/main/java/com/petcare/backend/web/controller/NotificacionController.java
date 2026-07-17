package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Notificacion;
import com.petcare.backend.domain.service.NotificacionService;
import com.petcare.backend.web.dto.response.NotificacionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> listarTodas() {
        return ResponseEntity.ok(notificacionService.listarTodas().stream().map(this::toResponse).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<Long> contarNoLeidas(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(notificacionService.contarNoLeidas(usuarioId));
    }

    @PatchMapping("/marcar-leidas")
    public ResponseEntity<Void> marcarComoLeidas(@RequestParam Long usuarioId) {
        notificacionService.marcarComoLeidas(usuarioId);
        return ResponseEntity.noContent().build();
    }

    private NotificacionResponse toResponse(Notificacion n) {
        return new NotificacionResponse(
                n.getId(), n.getTipo(), n.getDestinoUsuarioId(),
                n.getMascotaId(), n.getCitaId(), n.getCanal(),
                n.getMensaje(), n.getEstado(), n.getFechaEnvio(),
                n.getErrorMensaje(), n.getLeido(), n.getCreadoEn()
        );
    }
}
