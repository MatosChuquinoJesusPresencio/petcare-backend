package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AtencionClinicaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.AtencionClinicaRequest;
import com.petcare.backend.web.dto.response.AtencionClinicaResponse;
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
    public ResponseEntity<AtencionClinicaResponse> registrar(@Valid @RequestBody AtencionClinicaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario veterinario = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        AtencionClinica atencion = atencionClinicaService.registrar(
                request.appointmentId(), request.reasonForConsultation(), request.symptoms(),
                request.diagnosis(), request.treatment(), request.clinicalObservations(),
                veterinario.getId(), request.triageId());
        return new ResponseEntity<>(toResponse(atencion), HttpStatus.CREATED);
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<Page<AtencionClinicaResponse>> listarPorMascota(@PathVariable Long mascotaId, Pageable pageable) {
        return ResponseEntity.ok(atencionClinicaService.listarPorMascota(mascotaId, pageable).map(this::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtencionClinicaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(atencionClinicaService.obtenerPorId(id)));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<AtencionClinicaResponse> obtenerPorCitaId(@PathVariable Long citaId) {
        return ResponseEntity.ok(toResponse(atencionClinicaService.obtenerPorCitaId(citaId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<AtencionClinicaResponse> actualizar(@PathVariable Long id,
                                                               @Valid @RequestBody AtencionClinicaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        AtencionClinica actualizada = atencionClinicaService.actualizar(id,
                request.reasonForConsultation(), request.symptoms(), request.diagnosis(),
                request.treatment(), request.clinicalObservations(), usuario.getId());
        return ResponseEntity.ok(toResponse(actualizada));
    }

    private AtencionClinicaResponse toResponse(AtencionClinica a) {
        return new AtencionClinicaResponse(a.getId(),
                a.getCita().getId(), a.getMascota().getId(), a.getVeterinario().getId(),
                a.getTriaje() != null ? a.getTriaje().getId() : null,
                a.getMotivoConsulta(), a.getSintomas(), a.getDiagnostico(), a.getTratamiento(),
                a.getObservacionesClinicas(), a.getCreadoPor().getId(), a.getCreadoEn(),
                a.getActualizadoPor() != null ? a.getActualizadoPor().getId() : null,
                a.getActualizadoEn());
    }
}
