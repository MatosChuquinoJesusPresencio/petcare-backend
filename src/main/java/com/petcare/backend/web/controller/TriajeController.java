package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.TriajeService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.TriajeRequest;
import com.petcare.backend.web.dto.response.TriajeResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/triajes")
public class TriajeController {

    private final TriajeService triajeService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public TriajeController(TriajeService triajeService, UsuarioService usuarioService,
                            AuditoriaService auditoriaService) {
        this.triajeService = triajeService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<TriajeResponse> crearTriaje(@Valid @RequestBody TriajeRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario asistente = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));

        Triaje triaje = new Triaje(null, null, request.reasonForVisit(), request.urgencyLevel(),
                request.visibleSigns(), request.observations(), request.weight(), request.temperature(),
                request.heartRate(), request.respiratoryRate(), null, null, null);

        Triaje creado = triajeService.crearTriaje(triaje, request.appointmentId(), asistente.getId());

        try {
            LinkedHashMap<String, String> campos = new LinkedHashMap<>();
            campos.put("citaId", String.valueOf(request.appointmentId()));
            campos.put("motivoVisita", request.reasonForVisit());
            campos.put("nivelUrgencia", request.urgencyLevel());
            campos.put("signosVisibles", request.visibleSigns());
            campos.put("observaciones", request.observations());
            campos.put("peso", request.weight() != null ? String.valueOf(request.weight()) : null);
            campos.put("temperatura", request.temperature() != null ? String.valueOf(request.temperature()) : null);
            campos.put("frecuenciaCardiaca", request.heartRate() != null ? String.valueOf(request.heartRate()) : null);
            campos.put("frecuenciaRespiratoria", request.respiratoryRate() != null ? String.valueOf(request.respiratoryRate()) : null);
            auditoriaService.registrarCreacion("triajes", creado.getId(), asistente, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toResponse(creado), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<TriajeResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(triajeService.listarTodos(pageable).map(this::toResponse));
    }

    @GetMapping("/prioridad/{nivelUrgencia}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<TriajeResponse>> listarPorPrioridad(@PathVariable String nivelUrgencia, Pageable pageable) {
        return ResponseEntity.ok(triajeService.listarPorPrioridad(nivelUrgencia, pageable).map(this::toResponse));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<TriajeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(triajeService.obtenerPorId(id)));
    }

    @GetMapping("/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<TriajeResponse> obtenerPorCitaId(@PathVariable Long citaId) {
        return ResponseEntity.ok(toResponse(triajeService.obtenerPorCitaId(citaId)));
    }

    private TriajeResponse toResponse(Triaje t) {
        return new TriajeResponse(t.getId(),
                t.getCita() != null ? t.getCita().getId() : null,
                t.getMotivoVisita(),
                t.getNivelUrgencia(), t.getSignosVisibles(), t.getObservaciones(), t.getPeso(),
                t.getTemperatura(), t.getFrecuenciaCardiaca(), t.getFrecuenciaRespiratoria(),
                t.getAsistente() != null ? t.getAsistente().getId() : null,
                t.getCreadoEn(), t.getActualizadoEn());
    }
}
