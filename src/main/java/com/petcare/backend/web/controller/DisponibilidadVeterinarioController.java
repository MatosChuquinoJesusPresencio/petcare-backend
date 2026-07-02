package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.DisponibilidadVeterinarioService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.DisponibilidadRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadVeterinarioController {

    private final DisponibilidadVeterinarioService disponibilidadService;
    private final UsuarioService usuarioService;

    public DisponibilidadVeterinarioController(DisponibilidadVeterinarioService disponibilidadService,
                                                UsuarioService usuarioService) {
        this.disponibilidadService = disponibilidadService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<List<DisponibilidadVeterinario>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(disponibilidadService.listarPorVeterinario(veterinarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadVeterinario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinario> registrar(@Valid @RequestBody DisponibilidadRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        DisponibilidadVeterinario disponibilidad = DisponibilidadVeterinario.builder()
                .veterinario(veterinario)
                .diaSemana(request.diaSemana())
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();

        DisponibilidadVeterinario creada = disponibilidadService.registrar(disponibilidad);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinario> actualizar(@PathVariable Long id, @Valid @RequestBody DisponibilidadRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        DisponibilidadVeterinario detalles = DisponibilidadVeterinario.builder()
                .veterinario(veterinario)
                .diaSemana(request.diaSemana())
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .build();

        return ResponseEntity.ok(disponibilidadService.actualizar(id, detalles));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
