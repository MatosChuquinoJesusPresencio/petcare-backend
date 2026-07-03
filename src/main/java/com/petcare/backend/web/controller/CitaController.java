package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.CitaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.CitaEstadoRequest;
import com.petcare.backend.web.dto.request.CitaReprogramarRequest;
import com.petcare.backend.web.dto.request.CitaRequest;
import com.petcare.backend.web.dto.response.CitaResponse;
import com.petcare.backend.web.dto.response.DisponibilidadResponse;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<Page<CitaResponse>> listarCitas(
            @RequestParam(value = "mascotaId", required = false) Long mascotaId,
            @RequestParam(value = "veterinarioId", required = false) Long veterinarioId,
            @RequestParam(value = "servicioId", required = false) Long servicioId,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(value = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            Pageable pageable) {
        Page<CitaResponse> result;
        if (mascotaId != null || veterinarioId != null || servicioId != null
                || (estado != null && !estado.isBlank()) || fechaDesde != null || fechaHasta != null) {
            result = citaService.listarConFiltros(mascotaId, veterinarioId, servicioId, estado,
                            fechaDesde, fechaHasta, pageable)
                    .map(this::toCitaResponse);
        } else {
            result = citaService.listarTodas(pageable)
                    .map(this::toCitaResponse);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadResponse> obtenerDisponibilidad(
            @RequestParam Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long servicioId) {
        List<String> slots = citaService.obtenerSlotsDisponibles(veterinarioId, fecha, servicioId);
        DisponibilidadResponse response = new DisponibilidadResponse(veterinarioId, fecha.toString(), null, slots);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerPorId(id)
                .map(c -> ResponseEntity.ok(toCitaResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<Page<CitaResponse>> listarPorMascota(@PathVariable Long mascotaId, Pageable pageable) {
        return ResponseEntity.ok(citaService.listarPorMascota(mascotaId, pageable).map(this::toCitaResponse));
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<Page<CitaResponse>> listarPorVeterinario(@PathVariable Long veterinarioId, Pageable pageable) {
        return ResponseEntity.ok(citaService.listarPorVeterinario(veterinarioId, pageable).map(this::toCitaResponse));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<CitaResponse> agendarCita(@Valid @RequestBody CitaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not valid"));

        var cita = citaService.agendarCita(request.petId(), request.veterinarianId(),
                request.serviceId(), request.dateTime(), request.notes(), creador.getId());
        return new ResponseEntity<>(toCitaResponse(cita), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<CitaResponse> reprogramarCita(@PathVariable Long id,
                                                         @Valid @RequestBody CitaReprogramarRequest request) {
        var reprogramada = citaService.reprogramarCita(id, request.dateTime());
        return ResponseEntity.ok(toCitaResponse(reprogramada));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<CitaResponse> cambiarEstado(@PathVariable Long id,
                                                       @Valid @RequestBody CitaEstadoRequest request) {
        var actualizada = citaService.cambiarEstadoCita(id, request.status());
        return ResponseEntity.ok(toCitaResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long id) {
        citaService.cambiarEstadoCita(id, "CANCELADA");
        return ResponseEntity.noContent().build();
    }

    private CitaResponse toCitaResponse(com.petcare.backend.domain.model.Cita c) {
        return new CitaResponse(c.getId(), c.getMascota().getId(), c.getVeterinario().getId(),
                c.getServicio().getId(), c.getFechaHora(), c.getEstado(), c.getNotas(),
                c.getCreadoPor().getId(), c.getCreadoEn(),
                c.getActualizadoPor() != null ? c.getActualizadoPor().getId() : null,
                c.getActualizadoEn());
    }
}
