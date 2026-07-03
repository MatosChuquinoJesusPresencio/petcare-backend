package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.service.SalaEsperaService;
import com.petcare.backend.web.dto.request.SalaEsperaEstadoRequest;
import com.petcare.backend.web.dto.request.SalaEsperaRequest;
import com.petcare.backend.web.dto.response.SalaEsperaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sala-espera")
public class SalaEsperaController {

    private final SalaEsperaService salaEsperaService;

    public SalaEsperaController(SalaEsperaService salaEsperaService) {
        this.salaEsperaService = salaEsperaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<List<SalaEsperaResponse>> listarTodas() {
        return ResponseEntity.ok(toResponseList(salaEsperaService.listarTodas()));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<List<SalaEsperaResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(toResponseList(salaEsperaService.listarPorEstado(estado)));
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

    private List<SalaEsperaResponse> toResponseList(List<SalaEspera> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
