package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.SeguimientoService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.SeguimientoRequest;
import com.petcare.backend.web.dto.response.SeguimientoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public SeguimientoController(SeguimientoService seguimientoService, UsuarioService usuarioService,
                                 AuditoriaService auditoriaService) {
        this.seguimientoService = seguimientoService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping("/atenciones-clinicas/{atencionId}/seguimientos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<SeguimientoResponse> programar(@PathVariable Long atencionId,
                                                           @Valid @RequestBody SeguimientoRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        ZoneId zone = ZoneId.systemDefault();
        Seguimiento s = new Seguimiento();
        s.setTipo(request.tipo());
        s.setFechaProgramada(request.fechaProgramada().atZone(zone).toInstant());
        s.setMotivo(request.motivo());
        s.setDuenoNotificadoId(request.duenoNotificadoId());

        Seguimiento creado = seguimientoService.programar(atencionId, s, request.veterinarioId(), creador.getId());

        try {
            LinkedHashMap<String, String> campos = new LinkedHashMap<>();
            campos.put("atencionClinicaId", String.valueOf(atencionId));
            campos.put("veterinarioId", String.valueOf(request.veterinarioId()));
            campos.put("tipo", request.tipo());
            campos.put("fechaProgramada", String.valueOf(request.fechaProgramada()));
            campos.put("motivo", request.motivo());
            campos.put("estado", "PROGRAMADO");
            auditoriaService.registrarCreacion("seguimientos", creado.getId(), creador, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toResponse(creado), HttpStatus.CREATED);
    }

    @GetMapping("/atenciones-clinicas/{atencionId}/seguimientos")
    public ResponseEntity<List<SeguimientoResponse>> listarPorAtencion(@PathVariable Long atencionId) {
        return ResponseEntity.ok(seguimientoService.listarPorAtencion(atencionId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/seguimientos/proximos")
    public ResponseEntity<List<SeguimientoResponse>> proximos() {
        return ResponseEntity.ok(seguimientoService.obtenerProximos().stream().map(this::toResponse).toList());
    }

    @PatchMapping("/seguimientos/{id}/completar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<SeguimientoResponse> completar(@PathVariable Long id,
                                                           @RequestParam(required = false) String resultado) {
        Seguimiento existente = seguimientoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento no encontrado"));
        Seguimiento completado = seguimientoService.completar(id, resultado);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String[]> cambios = new LinkedHashMap<>();
            cambios.put("estado", new String[]{str(existente.getEstado()), str(completado.getEstado())});
            if (resultado != null) cambios.put("resultado", new String[]{null, resultado});
            auditoriaService.registrarEdicion("seguimientos", id, auditor, cambios, "Completar seguimiento");
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toResponse(completado));
    }

    @PatchMapping("/seguimientos/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<SeguimientoResponse> cancelar(@PathVariable Long id) {
        Seguimiento existente = seguimientoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento no encontrado"));
        Seguimiento cancelado = seguimientoService.cancelar(id);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String[]> cambios = new LinkedHashMap<>();
            cambios.put("estado", new String[]{str(existente.getEstado()), str(cancelado.getEstado())});
            auditoriaService.registrarEdicion("seguimientos", id, auditor, cambios, "Cancelación de seguimiento");
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toResponse(cancelado));
    }

    private SeguimientoResponse toResponse(Seguimiento s) {
        return new SeguimientoResponse(
                s.getId(), s.getAtencionClinicaId(),
                s.getMascota() != null ? s.getMascota().getId() : null,
                s.getMascota() != null ? s.getMascota().getNombre() : null,
                s.getVeterinario() != null ? s.getVeterinario().getId() : null,
                s.getVeterinario() != null ? s.getVeterinario().getNombres() + " " + s.getVeterinario().getApellidos() : null,
                s.getDuenoNotificadoId(), s.getTipo(), s.getFechaProgramada(),
                s.getFechaCompletada(), s.getMotivo(), s.getResultado(),
                s.getEstado(), s.getCreadoEn()
        );
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
