package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.CitaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.CitaEstadoRequest;
import com.petcare.backend.web.dto.CitaReprogramarRequest;
import com.petcare.backend.web.dto.CitaRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;

    public CitaController(CitaService citaService, UsuarioService usuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Page<Cita>> listarCitas(Pageable pageable) {
        return ResponseEntity.ok(citaService.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<Page<Cita>> listarPorMascota(@PathVariable Long mascotaId, Pageable pageable) {
        return ResponseEntity.ok(citaService.listarPorMascota(mascotaId, pageable));
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<Page<Cita>> listarPorVeterinario(@PathVariable Long veterinarioId, Pageable pageable) {
        return ResponseEntity.ok(citaService.listarPorVeterinario(veterinarioId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Cita> agendarCita(@Valid @RequestBody CitaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        Cita cita = citaService.agendarCita(
                request.mascotaId(),
                request.veterinarioId(),
                request.servicioId(),
                request.fechaHora(),
                request.notas(),
                creador.getId()
        );
        return new ResponseEntity<>(cita, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Cita> reprogramarCita(@PathVariable Long id, @Valid @RequestBody CitaReprogramarRequest request) {
        Cita reprogramada = citaService.reprogramarCita(id, request.fechaHora());
        return ResponseEntity.ok(reprogramada);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Cita> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CitaEstadoRequest request) {
        Cita actualizada = citaService.cambiarEstadoCita(id, request.estado());
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long id) {
        citaService.cambiarEstadoCita(id, "CANCELADA");
        return ResponseEntity.noContent().build();
    }
}
