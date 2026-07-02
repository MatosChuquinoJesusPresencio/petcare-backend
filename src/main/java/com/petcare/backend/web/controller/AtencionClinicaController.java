package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AtencionClinicaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.AtencionClinicaRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/atenciones-clinicas")
public class AtencionClinicaController {

    private final AtencionClinicaService atencionClinicaService;
    private final UsuarioService usuarioService;

    public AtencionClinicaController(AtencionClinicaService atencionClinicaService,
                                      UsuarioService usuarioService) {
        this.atencionClinicaService = atencionClinicaService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<AtencionClinica> registrar(@Valid @RequestBody AtencionClinicaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario veterinario = usuarioService.obtenerPorUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        AtencionClinica atencion = atencionClinicaService.registrar(
                request.citaId(),
                request.motivoConsulta(),
                request.sintomas(),
                request.diagnostico(),
                request.observacionesClinicas(),
                veterinario.getId(),
                request.triajeId()
        );
        return new ResponseEntity<>(atencion, HttpStatus.CREATED);
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<Page<AtencionClinica>> listarPorMascota(@PathVariable Long mascotaId, Pageable pageable) {
        return ResponseEntity.ok(atencionClinicaService.listarPorMascota(mascotaId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtencionClinica> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(atencionClinicaService.obtenerPorId(id));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<AtencionClinica> obtenerPorCitaId(@PathVariable Long citaId) {
        return ResponseEntity.ok(atencionClinicaService.obtenerPorCitaId(citaId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<AtencionClinica> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody AtencionClinicaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        AtencionClinica actualizada = atencionClinicaService.actualizar(
                id,
                request.motivoConsulta(),
                request.sintomas(),
                request.diagnostico(),
                request.observacionesClinicas(),
                usuario.getId()
        );
        return ResponseEntity.ok(actualizada);
    }
}
