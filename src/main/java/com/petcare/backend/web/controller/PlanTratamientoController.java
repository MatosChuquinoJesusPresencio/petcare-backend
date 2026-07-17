package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.PlanTratamiento;
import com.petcare.backend.domain.model.PlanTratamientoActividad;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.PlanTratamientoService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.PlanTratamientoRequest;
import com.petcare.backend.web.dto.response.ActividadResponse;
import com.petcare.backend.web.dto.response.PlanTratamientoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlanTratamientoController {

    private final PlanTratamientoService planService;
    private final UsuarioService usuarioService;

    public PlanTratamientoController(PlanTratamientoService planService, UsuarioService usuarioService) {
        this.planService = planService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/atenciones-clinicas/{atencionId}/planes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<PlanTratamientoResponse> crear(@PathVariable Long atencionId,
                                                           @Valid @RequestBody PlanTratamientoRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        PlanTratamiento plan = new PlanTratamiento();
        plan.setTitulo(request.titulo());
        plan.setDescripcion(request.descripcion());
        plan.setFechaInicio(request.fechaInicio());
        plan.setFechaFinEstimada(request.fechaFinEstimada());
        if (request.actividades() != null) {
            plan.setActividades(request.actividades().stream().map(a -> {
                var act = new PlanTratamientoActividad();
                act.setTipo(a.tipo());
                act.setDescripcion(a.descripcion());
                act.setFechaProgramada(a.fechaProgramada());
                act.setHoraProgramada(a.horaProgramada());
                act.setFrecuencia(a.frecuencia());
                act.setResponsable(a.responsable());
                act.setObservaciones(a.observaciones());
                return act;
            }).toList());
        }

        PlanTratamiento creado = planService.crear(atencionId, plan, request.veterinarioId(), creador.getId());
        return new ResponseEntity<>(toResponse(creado), HttpStatus.CREATED);
    }

    @GetMapping("/atenciones-clinicas/{atencionId}/planes")
    public ResponseEntity<List<PlanTratamientoResponse>> listarPorAtencion(@PathVariable Long atencionId) {
        return ResponseEntity.ok(planService.listarPorAtencion(atencionId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/mascotas/{mascotaId}/planes")
    public ResponseEntity<List<PlanTratamientoResponse>> listarPorMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(planService.listarPorMascota(mascotaId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/planes/{id}")
    public ResponseEntity<PlanTratamientoResponse> obtenerPorId(@PathVariable Long id) {
        return planService.obtenerPorId(id)
                .map(p -> ResponseEntity.ok(toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/planes/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<PlanTratamientoResponse> actualizar(@PathVariable Long id,
                                                                @Valid @RequestBody PlanTratamientoRequest request) {
        PlanTratamiento detalles = new PlanTratamiento();
        detalles.setTitulo(request.titulo());
        detalles.setDescripcion(request.descripcion());
        detalles.setFechaInicio(request.fechaInicio());
        detalles.setFechaFinEstimada(request.fechaFinEstimada());
        if (request.actividades() != null) {
            detalles.setActividades(request.actividades().stream().map(a -> {
                var act = new PlanTratamientoActividad();
                act.setTipo(a.tipo());
                act.setDescripcion(a.descripcion());
                act.setFechaProgramada(a.fechaProgramada());
                act.setHoraProgramada(a.horaProgramada());
                act.setFrecuencia(a.frecuencia());
                act.setResponsable(a.responsable());
                act.setObservaciones(a.observaciones());
                return act;
            }).toList());
        }
        return ResponseEntity.ok(toResponse(planService.actualizar(id, detalles)));
    }

    @PatchMapping("/planes/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<PlanTratamientoResponse> cambiarEstado(@PathVariable Long id,
                                                                   @RequestParam String estado) {
        return ResponseEntity.ok(toResponse(planService.cambiarEstado(id, estado)));
    }

    private PlanTratamientoResponse toResponse(PlanTratamiento p) {
        List<ActividadResponse> actividades = p.getActividades() != null
                ? p.getActividades().stream().map(a -> new ActividadResponse(
                        a.getId(), a.getTipo(), a.getDescripcion(),
                        a.getFechaProgramada(), a.getHoraProgramada(),
                        a.getFrecuencia(), a.getResponsable(), a.getEstado(), a.getObservaciones()))
                .toList()
                : List.of();

        return new PlanTratamientoResponse(
                p.getId(),
                p.getMascota() != null ? p.getMascota().getId() : null,
                p.getMascota() != null ? p.getMascota().getNombre() : null,
                p.getAtencionClinicaId(),
                p.getVeterinario() != null ? p.getVeterinario().getId() : null,
                p.getVeterinario() != null ? p.getVeterinario().getNombres() + " " + p.getVeterinario().getApellidos() : null,
                p.getTitulo(),
                p.getDescripcion(),
                p.getFechaInicio(),
                p.getFechaFinEstimada(),
                p.getEstado(),
                p.getCreadoPor() != null ? p.getCreadoPor().getId() : null,
                p.getCreadoEn(),
                actividades
        );
    }
}
