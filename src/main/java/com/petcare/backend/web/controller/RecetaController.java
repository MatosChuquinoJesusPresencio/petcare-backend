package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Receta;
import com.petcare.backend.domain.model.RecetaDetalle;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.RecetaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.RecetaRequest;
import com.petcare.backend.web.dto.response.RecetaDetalleResponse;
import com.petcare.backend.web.dto.response.RecetaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecetaController {

    private final RecetaService recetaService;
    private final UsuarioService usuarioService;

    public RecetaController(RecetaService recetaService, UsuarioService usuarioService) {
        this.recetaService = recetaService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/atenciones-clinicas/{atencionId}/recetas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<RecetaResponse> crear(@PathVariable Long atencionId,
                                                 @Valid @RequestBody RecetaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        Receta receta = new Receta();
        receta.setDiagnostico(request.diagnostico());
        receta.setNotasAdicionales(request.notasAdicionales());
        if (request.detalles() != null) {
            receta.setDetalles(request.detalles().stream().map(d -> {
                var det = new RecetaDetalle();
                det.setMedicamento(d.medicamento());
                det.setPresentacion(d.presentacion());
                det.setDosis(d.dosis());
                det.setFrecuencia(d.frecuencia());
                det.setDuracion(d.duracion());
                det.setViaAdministracion(d.viaAdministracion());
                det.setIndicaciones(d.indicaciones());
                return det;
            }).toList());
        }

        Receta creada = recetaService.crear(atencionId, receta, request.veterinarioId(), creador.getId());
        return new ResponseEntity<>(toResponse(creada), HttpStatus.CREATED);
    }

    @GetMapping("/atenciones-clinicas/{atencionId}/recetas")
    public ResponseEntity<List<RecetaResponse>> listarPorAtencion(@PathVariable Long atencionId) {
        List<Receta> recetas = recetaService.listarPorAtencion(atencionId);
        return ResponseEntity.ok(recetas.stream().map(this::toResponse).toList());
    }

    @GetMapping("/recetas/{id}")
    public ResponseEntity<RecetaResponse> obtenerPorId(@PathVariable Long id) {
        return recetaService.obtenerPorId(id)
                .map(r -> ResponseEntity.ok(toResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/recetas/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<RecetaResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody RecetaRequest request) {
        Receta detalles = new Receta();
        detalles.setDiagnostico(request.diagnostico());
        detalles.setNotasAdicionales(request.notasAdicionales());
        if (request.detalles() != null) {
            detalles.setDetalles(request.detalles().stream().map(d -> {
                var det = new RecetaDetalle();
                det.setMedicamento(d.medicamento());
                det.setPresentacion(d.presentacion());
                det.setDosis(d.dosis());
                det.setFrecuencia(d.frecuencia());
                det.setDuracion(d.duracion());
                det.setViaAdministracion(d.viaAdministracion());
                det.setIndicaciones(d.indicaciones());
                return det;
            }).toList());
        }

        Receta actualizada = recetaService.actualizar(id, detalles);
        return ResponseEntity.ok(toResponse(actualizada));
    }

    @GetMapping("/mascotas/{mascotaId}/recetas")
    public ResponseEntity<List<RecetaResponse>> listarPorMascota(@PathVariable Long mascotaId) {
        List<Receta> recetas = recetaService.listarPorMascota(mascotaId);
        return ResponseEntity.ok(recetas.stream().map(this::toResponse).toList());
    }

    private RecetaResponse toResponse(Receta r) {
        List<RecetaDetalleResponse> detalles = r.getDetalles() != null
                ? r.getDetalles().stream().map(d -> new RecetaDetalleResponse(
                        d.getId(), d.getMedicamento(), d.getPresentacion(),
                        d.getDosis(), d.getFrecuencia(), d.getDuracion(),
                        d.getViaAdministracion(), d.getIndicaciones()))
                .toList()
                : List.of();

        return new RecetaResponse(
                r.getId(),
                r.getAtencionClinicaId(),
                r.getMascota() != null ? r.getMascota().getId() : null,
                r.getMascota() != null ? r.getMascota().getNombre() : null,
                r.getVeterinario() != null ? r.getVeterinario().getId() : null,
                r.getVeterinario() != null ? r.getVeterinario().getNombres() + " " + r.getVeterinario().getApellidos() : null,
                r.getDiagnostico(),
                r.getNotasAdicionales(),
                r.getEstado(),
                r.getCreadoPor() != null ? r.getCreadoPor().getId() : null,
                r.getCreadoEn(),
                detalles
        );
    }
}
