package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.DisponibilidadVeterinarioService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.DisponibilidadRequest;
import com.petcare.backend.web.dto.response.DisponibilidadVeterinarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadVeterinarioController {

    private final DisponibilidadVeterinarioService disponibilidadService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public DisponibilidadVeterinarioController(DisponibilidadVeterinarioService disponibilidadService,
                                                UsuarioService usuarioService,
                                                AuditoriaService auditoriaService) {
        this.disponibilidadService = disponibilidadService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<List<DisponibilidadVeterinarioResponse>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(toResponseList(disponibilidadService.listarPorVeterinario(veterinarioId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<DisponibilidadVeterinarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(disponibilidadService.obtenerPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinarioResponse> registrar(@Valid @RequestBody DisponibilidadRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));

        DisponibilidadVeterinario disponibilidad = new DisponibilidadVeterinario(null, veterinario,
                request.dayOfWeek(), request.startTime(), request.endTime(), true);

        DisponibilidadVeterinario creada = disponibilidadService.registrar(disponibilidad);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String> campos = new LinkedHashMap<>();
            campos.put("veterinarioId", String.valueOf(request.veterinarianId()));
            campos.put("diaSemana", String.valueOf(request.dayOfWeek()));
            campos.put("horaInicio", String.valueOf(request.startTime()));
            campos.put("horaFin", String.valueOf(request.endTime()));
            auditoriaService.registrarCreacion("disponibilidad_veterinarios", creada.getId(), auditor, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toResponse(creada), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinarioResponse> actualizar(@PathVariable Long id,
                                                                         @Valid @RequestBody DisponibilidadRequest request) {
        DisponibilidadVeterinario existente = disponibilidadService.obtenerPorId(id);
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));

        DisponibilidadVeterinario detalles = new DisponibilidadVeterinario(null, veterinario,
                request.dayOfWeek(), request.startTime(), request.endTime(), null);

        DisponibilidadVeterinario actualizada = disponibilidadService.actualizar(id, detalles);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String[]> cambios = new LinkedHashMap<>();
            if (!str(existente.getDiaSemana()).equals(str(actualizada.getDiaSemana())))
                cambios.put("diaSemana", new String[]{str(existente.getDiaSemana()), str(actualizada.getDiaSemana())});
            if (!str(existente.getHoraInicio()).equals(str(actualizada.getHoraInicio())))
                cambios.put("horaInicio", new String[]{str(existente.getHoraInicio()), str(actualizada.getHoraInicio())});
            if (!str(existente.getHoraFin()).equals(str(actualizada.getHoraFin())))
                cambios.put("horaFin", new String[]{str(existente.getHoraFin()), str(actualizada.getHoraFin())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("disponibilidad_veterinarios", id, auditor, cambios, "Actualización de disponibilidad");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toResponse(actualizada));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<DisponibilidadVeterinarioResponse> toggleActivo(@PathVariable Long id) {
        DisponibilidadVeterinario existente = disponibilidadService.obtenerPorId(id);
        DisponibilidadVeterinario toggled = disponibilidadService.toggleActivo(id);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String[]> cambios = new LinkedHashMap<>();
            cambios.put("activo", new String[]{str(existente.getActivo()), str(toggled.getActivo())});
            auditoriaService.registrarEdicion("disponibilidad_veterinarios", id, auditor, cambios, "Cambio de estado activo");
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toResponse(toggled));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        DisponibilidadVeterinario existente = disponibilidadService.obtenerPorId(id);
        disponibilidadService.eliminar(id);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario auditor = usuarioService.obtenerPorEmail(username).orElse(null);
            LinkedHashMap<String, String> campos = new LinkedHashMap<>();
            campos.put("veterinarioId", existente.getVeterinario() != null ? String.valueOf(existente.getVeterinario().getId()) : "-");
            campos.put("diaSemana", str(existente.getDiaSemana()));
            campos.put("horaInicio", str(existente.getHoraInicio()));
            campos.put("horaFin", str(existente.getHoraFin()));
            auditoriaService.registrarEliminacion("disponibilidad_veterinarios", id, auditor, campos);
        } catch (Exception ignored) { }

        return ResponseEntity.noContent().build();
    }

    private DisponibilidadVeterinarioResponse toResponse(DisponibilidadVeterinario d) {
        return new DisponibilidadVeterinarioResponse(d.getId(),
                d.getVeterinario() != null ? d.getVeterinario().getId() : null,
                d.getDiaSemana(), d.getHoraInicio(), d.getHoraFin(), d.getActivo());
    }

    private List<DisponibilidadVeterinarioResponse> toResponseList(List<DisponibilidadVeterinario> list) {
        return list.stream().map(this::toResponse).toList();
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
