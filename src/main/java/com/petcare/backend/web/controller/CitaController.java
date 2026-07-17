package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.CitaService;
import com.petcare.backend.domain.service.ServicioService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final ServicioService servicioService;
    private final AuditoriaService auditoriaService;

    public CitaController(CitaService citaService, UsuarioService usuarioService,
                          ServicioService servicioService, AuditoriaService auditoriaService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.servicioService = servicioService;
        this.auditoriaService = auditoriaService;
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
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
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
        Integer duracion = servicioService.obtenerPorId(servicioId)
                .map(s -> s.getDuracionMinutos())
                .orElse(null);
        DisponibilidadResponse response = new DisponibilidadResponse(veterinarioId, fecha.toString(), duracion, slots);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> obtenerCita(@PathVariable Long id) {
        return citaService.obtenerPorId(id)
                .map(c -> ResponseEntity.ok(toCitaResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<Page<CitaResponse>> listarPorMascota(@PathVariable Long mascotaId, @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(citaService.listarPorMascota(mascotaId, pageable).map(this::toCitaResponse));
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<Page<CitaResponse>> listarPorVeterinario(@PathVariable Long veterinarioId, @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(citaService.listarPorVeterinario(veterinarioId, pageable).map(this::toCitaResponse));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<CitaResponse> agendarCita(@Valid @RequestBody CitaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        var cita = citaService.agendarCita(request.petId(), request.veterinarianId(),
                request.serviceId(), request.dateTime(), request.notes(), creador.getId());

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String> campos = new java.util.LinkedHashMap<>();
            campos.put("mascotaId", String.valueOf(request.petId()));
            campos.put("veterinarioId", String.valueOf(request.veterinarianId()));
            campos.put("servicioId", String.valueOf(request.serviceId()));
            campos.put("fechaHora", String.valueOf(request.dateTime()));
            campos.put("estado", cita.getEstado());
            campos.put("notas", request.notes());
            auditoriaService.registrarCreacion("citas", cita.getId(), auditor, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toCitaResponse(cita), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/reprogramar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<CitaResponse> reprogramarCita(@PathVariable Long id,
                                                         @Valid @RequestBody CitaReprogramarRequest request) {
        Cita existente = citaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        var reprogramada = citaService.reprogramarCita(id, request.dateTime());

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String[]> cambios = new java.util.LinkedHashMap<>();
            if (!str(existente.getFechaHora()).equals(str(reprogramada.getFechaHora())))
                cambios.put("fechaHora", new String[]{str(existente.getFechaHora()), str(reprogramada.getFechaHora())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("citas", id, auditor, cambios, "Reprogramación de cita");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toCitaResponse(reprogramada));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<CitaResponse> cambiarEstado(@PathVariable Long id,
                                                       @Valid @RequestBody CitaEstadoRequest request) {
        Cita existente = citaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        var actualizada = citaService.cambiarEstadoCita(id, request.status());

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String[]> cambios = new java.util.LinkedHashMap<>();
            if (!str(existente.getEstado()).equals(str(actualizada.getEstado())))
                cambios.put("estado", new String[]{str(existente.getEstado()), str(actualizada.getEstado())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("citas", id, auditor, cambios, "Cambio de estado de cita");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toCitaResponse(actualizada));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long id) {
        Cita existente = citaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        citaService.cambiarEstadoCita(id, "CANCELADA");

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String[]> cambios = new java.util.LinkedHashMap<>();
            cambios.put("estado", new String[]{str(existente.getEstado()), "CANCELADA"});
            auditoriaService.registrarEdicion("citas", id, auditor, cambios, "Cancelación de cita");
        } catch (Exception ignored) { }

        return ResponseEntity.noContent().build();
    }

    private CitaResponse toCitaResponse(com.petcare.backend.domain.model.Cita c) {
        return new CitaResponse(c.getId(),
                c.getMascota() != null ? c.getMascota().getId() : null,
                c.getVeterinario() != null ? c.getVeterinario().getId() : null,
                c.getServicio() != null ? c.getServicio().getId() : null,
                c.getFechaHora(), c.getEstado(), c.getNotas(),
                c.getCreadoPor() != null ? c.getCreadoPor().getId() : null,
                c.getCreadoEn(),
                c.getActualizadoPor() != null ? c.getActualizadoPor().getId() : null,
                c.getActualizadoEn());
    }

    private Usuario obtenerUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.obtenerPorEmail(username).orElse(null);
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
