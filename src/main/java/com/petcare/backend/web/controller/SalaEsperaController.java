package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.SalaEsperaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.web.dto.request.SalaEsperaEstadoRequest;
import com.petcare.backend.web.dto.request.SalaEsperaRequest;
import com.petcare.backend.web.dto.response.SalaEsperaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/sala-espera")
public class SalaEsperaController {

    private final SalaEsperaService salaEsperaService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public SalaEsperaController(SalaEsperaService salaEsperaService, UsuarioService usuarioService,
                                AuditoriaService auditoriaService) {
        this.salaEsperaService = salaEsperaService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<SalaEsperaResponse>> listarTodas(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(toResponsePage(salaEsperaService.listarTodas(pageable)));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<SalaEsperaResponse>> listarPorEstado(
            @PathVariable String estado,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(toResponsePage(salaEsperaService.listarPorEstado(estado, pageable)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<SalaEsperaResponse> registrarLlegada(@Valid @RequestBody SalaEsperaRequest request) {
        SalaEspera entrada = salaEsperaService.registrarLlegada(request.appointmentId(), request.observations());

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String> campos = new LinkedHashMap<>();
            campos.put("citaId", String.valueOf(request.appointmentId()));
            campos.put("observaciones", request.observations());
            campos.put("estado", "PENDIENTE");
            auditoriaService.registrarCreacion("sala_espera", entrada.getId(), auditor, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toResponse(entrada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<SalaEsperaResponse> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody SalaEsperaEstadoRequest request) {
        SalaEspera existente = salaEsperaService.obtenerPorId(id);
        SalaEspera actualizada = salaEsperaService.cambiarEstado(id, request.status());

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String[]> cambios = new LinkedHashMap<>();
            if (!str(existente.getEstado()).equals(str(actualizada.getEstado())))
                cambios.put("estado", new String[]{str(existente.getEstado()), str(actualizada.getEstado())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("sala_espera", id, auditor, cambios, "Cambio de estado en sala de espera");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toResponse(actualizada));
    }

    private SalaEsperaResponse toResponse(SalaEspera s) {
        return new SalaEsperaResponse(s.getId(),
                s.getCita() != null ? s.getCita().getId() : null,
                s.getMascota() != null ? s.getMascota().getId() : null,
                s.getFechaLlegada(), s.getEstado(), s.getObservaciones());
    }

    private Page<SalaEsperaResponse> toResponsePage(Page<SalaEspera> page) {
        return page.map(this::toResponse);
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
