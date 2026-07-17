package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.HistorialVacunacion;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.HistorialVacunacionService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.HistorialVacunacionRequest;
import com.petcare.backend.web.dto.response.HistorialVacunacionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistorialVacunacionController {

    private final HistorialVacunacionService vacunacionService;
    private final UsuarioService usuarioService;

    public HistorialVacunacionController(HistorialVacunacionService vacunacionService,
                                          UsuarioService usuarioService) {
        this.vacunacionService = vacunacionService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mascotas/{mascotaId}/vacunaciones")
    public ResponseEntity<List<HistorialVacunacionResponse>> listarPorMascota(@PathVariable Long mascotaId) {
        List<HistorialVacunacion> registros = vacunacionService.listarPorMascota(mascotaId);
        return ResponseEntity.ok(registros.stream().map(this::toResponse).toList());
    }

    @PostMapping("/mascotas/{mascotaId}/vacunaciones")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<HistorialVacunacionResponse> registrar(@PathVariable Long mascotaId,
                                                                   @Valid @RequestBody HistorialVacunacionRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        HistorialVacunacion vacunacion = new HistorialVacunacion();
        vacunacion.setTipo(request.tipo());
        vacunacion.setNombreProducto(request.nombreProducto());
        vacunacion.setFechaAplicacion(request.fechaAplicacion());
        vacunacion.setProximaDosis(request.proximaDosis());
        vacunacion.setLote(request.lote());
        vacunacion.setFabricante(request.fabricante());
        vacunacion.setDosis(request.dosis());
        vacunacion.setViaAdministracion(request.viaAdministracion());
        vacunacion.setObservaciones(request.observaciones());

        HistorialVacunacion creada = vacunacionService.registrar(
                mascotaId, vacunacion, request.veterinarioId(), creador.getId());

        return new ResponseEntity<>(toResponse(creada), HttpStatus.CREATED);
    }

    @PutMapping("/vacunaciones/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<HistorialVacunacionResponse> actualizar(@PathVariable Long id,
                                                                    @Valid @RequestBody HistorialVacunacionRequest request) {
        HistorialVacunacion detalles = new HistorialVacunacion();
        detalles.setTipo(request.tipo());
        detalles.setNombreProducto(request.nombreProducto());
        detalles.setFechaAplicacion(request.fechaAplicacion());
        detalles.setProximaDosis(request.proximaDosis());
        detalles.setLote(request.lote());
        detalles.setFabricante(request.fabricante());
        detalles.setDosis(request.dosis());
        detalles.setViaAdministracion(request.viaAdministracion());
        detalles.setObservaciones(request.observaciones());
        detalles.setEstado("APLICADA");

        HistorialVacunacion actualizada = vacunacionService.actualizar(id, detalles);
        return ResponseEntity.ok(toResponse(actualizada));
    }

    @DeleteMapping("/vacunaciones/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vacunacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vacunaciones/proximas")
    public ResponseEntity<List<HistorialVacunacionResponse>> proximasDosis() {
        List<HistorialVacunacion> registros = vacunacionService.obtenerProximasDosis();
        return ResponseEntity.ok(registros.stream().map(this::toResponse).toList());
    }

    private HistorialVacunacionResponse toResponse(HistorialVacunacion v) {
        return new HistorialVacunacionResponse(
                v.getId(),
                v.getMascota() != null ? v.getMascota().getId() : null,
                v.getMascota() != null ? v.getMascota().getNombre() : null,
                v.getTipo(),
                v.getNombreProducto(),
                v.getFechaAplicacion(),
                v.getProximaDosis(),
                v.getLote(),
                v.getFabricante(),
                v.getDosis(),
                v.getViaAdministracion(),
                v.getVeterinario() != null ? v.getVeterinario().getId() : null,
                v.getVeterinario() != null ? v.getVeterinario().getNombres() + " " + v.getVeterinario().getApellidos() : null,
                v.getObservaciones(),
                v.getEstado()
        );
    }
}
