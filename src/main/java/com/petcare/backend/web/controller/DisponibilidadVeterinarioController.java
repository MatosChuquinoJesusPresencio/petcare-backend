package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.DisponibilidadVeterinarioService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.DisponibilidadRequest;
import com.petcare.backend.web.dto.response.DisponibilidadVeterinarioResponse;
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
    public ResponseEntity<List<DisponibilidadVeterinarioResponse>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(toResponseList(disponibilidadService.listarPorVeterinario(veterinarioId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadVeterinarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(disponibilidadService.obtenerPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinarioResponse> registrar(@Valid @RequestBody DisponibilidadRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        DisponibilidadVeterinario disponibilidad = new DisponibilidadVeterinario(null, veterinario,
                request.dayOfWeek(), request.startTime(), request.endTime(), true);

        DisponibilidadVeterinario creada = disponibilidadService.registrar(disponibilidad);
        return new ResponseEntity<>(toResponse(creada), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinarioResponse> actualizar(@PathVariable Long id,
                                                                         @Valid @RequestBody DisponibilidadRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        DisponibilidadVeterinario detalles = new DisponibilidadVeterinario(null, veterinario,
                request.dayOfWeek(), request.startTime(), request.endTime(), null);

        return ResponseEntity.ok(toResponse(disponibilidadService.actualizar(id, detalles)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private DisponibilidadVeterinarioResponse toResponse(DisponibilidadVeterinario d) {
        return new DisponibilidadVeterinarioResponse(d.getId(), d.getVeterinario().getId(),
                d.getDiaSemana(), d.getHoraInicio(), d.getHoraFin(), d.getActivo());
    }

    private List<DisponibilidadVeterinarioResponse> toResponseList(List<DisponibilidadVeterinario> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
