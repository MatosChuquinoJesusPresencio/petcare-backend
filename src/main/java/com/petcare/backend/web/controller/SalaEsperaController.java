package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.service.SalaEsperaService;
import com.petcare.backend.web.dto.request.SalaEsperaEstadoRequest;
import com.petcare.backend.web.dto.request.SalaEsperaRequest;
import com.petcare.backend.web.dto.response.SalaEsperaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sala-espera")
public class SalaEsperaController {

    private final SalaEsperaService salaEsperaService;

    public SalaEsperaController(SalaEsperaService salaEsperaService) {
        this.salaEsperaService = salaEsperaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<SalaEsperaResponse>> listarTodas(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(toResponsePage(salaEsperaService.listarTodas(pageable)));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<SalaEsperaResponse>> listarPorEstado(
            @PathVariable String estado,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(toResponsePage(salaEsperaService.listarPorEstado(estado, pageable)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<SalaEsperaResponse> registrarLlegada(@Valid @RequestBody SalaEsperaRequest request) {
        SalaEspera entrada = salaEsperaService.registrarLlegada(request.appointmentId(), request.observations());
        return new ResponseEntity<>(toResponse(entrada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<SalaEsperaResponse> cambiarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody SalaEsperaEstadoRequest request) {
        return ResponseEntity.ok(toResponse(salaEsperaService.cambiarEstado(id, request.status())));
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
}
