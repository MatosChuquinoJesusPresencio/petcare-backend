package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.service.SalaEsperaService;
import com.petcare.backend.web.dto.SalaEsperaEstadoRequest;
import com.petcare.backend.web.dto.SalaEsperaRequest;
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
    public ResponseEntity<List<SalaEspera>> listarTodas() {
        return ResponseEntity.ok(salaEsperaService.listarTodas());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SalaEspera>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(salaEsperaService.listarPorEstado(estado));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<SalaEspera> registrarLlegada(@Valid @RequestBody SalaEsperaRequest request) {
        SalaEspera entrada = salaEsperaService.registrarLlegada(request.citaId(), request.observaciones());
        return new ResponseEntity<>(entrada, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<SalaEspera> cambiarEstado(@PathVariable Long id,
                                                     @Valid @RequestBody SalaEsperaEstadoRequest request) {
        return ResponseEntity.ok(salaEsperaService.cambiarEstado(id, request.estado()));
    }
}
