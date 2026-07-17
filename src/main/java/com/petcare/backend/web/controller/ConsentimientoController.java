package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.service.ConsentimientoService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.ConsentimientoRequest;
import com.petcare.backend.web.dto.response.ConsentimientoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ConsentimientoController {

    private final ConsentimientoService consentimientoService;
    private final UsuarioService usuarioService;

    public ConsentimientoController(ConsentimientoService consentimientoService, UsuarioService usuarioService) {
        this.consentimientoService = consentimientoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/consentimientos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<ConsentimientoResponse> registrar(@Valid @RequestBody ConsentimientoRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario creador = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no válido"));

        Consentimiento c = new Consentimiento();
        c.setDuenoId(request.duenoId());
        c.setAtencionClinicaId(request.atencionClinicaId());
        c.setTipoProcedimiento(request.tipoProcedimiento());
        c.setDescripcionProcedimiento(request.descripcionProcedimiento());
        c.setRiesgosDescritos(request.riesgosDescritos());
        c.setAlternativas(request.alternativas());
        c.setConsentido(request.consentido());
        c.setDuenoNombreVerificado(request.duenoNombreVerificado());
        c.setTestigoNombre(request.testigoNombre());
        c.setObservaciones(request.observaciones());

        Consentimiento creado = consentimientoService.registrar(c, request.mascotaId(), request.veterinarioId(), creador.getId());
        return new ResponseEntity<>(toResponse(creado), HttpStatus.CREATED);
    }

    @GetMapping("/consentimientos")
    public ResponseEntity<List<ConsentimientoResponse>> listarTodos() {
        return ResponseEntity.ok(consentimientoService.listarTodos().stream().map(this::toResponse).toList());
    }

    @GetMapping("/mascotas/{mascotaId}/consentimientos")
    public ResponseEntity<List<ConsentimientoResponse>> listarPorMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(consentimientoService.listarPorMascota(mascotaId).stream().map(this::toResponse).toList());
    }

    @GetMapping("/consentimientos/{id}")
    public ResponseEntity<ConsentimientoResponse> obtenerPorId(@PathVariable Long id) {
        return consentimientoService.obtenerPorId(id)
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ConsentimientoResponse toResponse(Consentimiento c) {
        return new ConsentimientoResponse(
                c.getId(),
                c.getMascota() != null ? c.getMascota().getId() : null,
                c.getMascota() != null ? c.getMascota().getNombre() : null,
                c.getDuenoId(),
                c.getAtencionClinicaId(),
                c.getVeterinario() != null ? c.getVeterinario().getId() : null,
                c.getVeterinario() != null ? c.getVeterinario().getNombres() + " " + c.getVeterinario().getApellidos() : null,
                c.getTipoProcedimiento(), c.getDescripcionProcedimiento(),
                c.getRiesgosDescritos(), c.getAlternativas(),
                c.getConsentido(), c.getFechaConsentimiento(),
                c.getDuenoNombreVerificado(), c.getTestigoNombre(),
                c.getObservaciones(), c.getCreadoEn()
        );
    }
}
