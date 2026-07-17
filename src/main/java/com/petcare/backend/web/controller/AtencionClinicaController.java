package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AtencionClinicaService;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.AtencionClinicaRequest;
import com.petcare.backend.web.dto.response.AtencionClinicaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final AuditoriaService auditoriaService;

    public AtencionClinicaController(AtencionClinicaService atencionClinicaService,
                                      UsuarioService usuarioService,
                                      AuditoriaService auditoriaService) {
        this.atencionClinicaService = atencionClinicaService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<AtencionClinicaResponse> registrar(@Valid @RequestBody AtencionClinicaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario veterinario = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));

        AtencionClinica atencion = atencionClinicaService.registrar(
                request.appointmentId(), request.reasonForConsultation(), request.symptoms(),
                request.diagnosis(), request.treatment(), request.clinicalObservations(),
                veterinario.getId(), request.triageId());

        try {
            java.util.LinkedHashMap<String, String> campos = new java.util.LinkedHashMap<>();
            campos.put("citaId", String.valueOf(request.appointmentId()));
            campos.put("motivoConsulta", atencion.getMotivoConsulta());
            campos.put("sintomas", atencion.getSintomas());
            campos.put("diagnostico", atencion.getDiagnostico());
            campos.put("tratamiento", atencion.getTratamiento());
            campos.put("observacionesClinicas", atencion.getObservacionesClinicas());
            auditoriaService.registrarCreacion("atenciones_clinicas", atencion.getId(), veterinario, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toResponse(atencion), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<Page<AtencionClinicaResponse>> listarTodas(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(atencionClinicaService.listarTodas(pageable).map(this::toResponse));
    }

    @GetMapping("/mascota/{mascotaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<Page<AtencionClinicaResponse>> listarPorMascota(@PathVariable Long mascotaId, @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(atencionClinicaService.listarPorMascota(mascotaId, pageable).map(this::toResponse));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<AtencionClinicaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(atencionClinicaService.obtenerPorId(id)));
    }

    @GetMapping("/cita/{citaId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO', 'ASISTENTE')")
    public ResponseEntity<AtencionClinicaResponse> obtenerPorCitaId(@PathVariable Long citaId) {
        return ResponseEntity.ok(toResponse(atencionClinicaService.obtenerPorCitaId(citaId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<AtencionClinicaResponse> actualizar(@PathVariable Long id,
                                                               @Valid @RequestBody AtencionClinicaRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));

        AtencionClinica existente = atencionClinicaService.obtenerPorId(id);
        AtencionClinica actualizada = atencionClinicaService.actualizar(id,
                request.reasonForConsultation(), request.symptoms(), request.diagnosis(),
                request.treatment(), request.clinicalObservations(), usuario.getId());

        try {
            java.util.LinkedHashMap<String, String[]> cambios = new java.util.LinkedHashMap<>();
            if (!str(existente.getMotivoConsulta()).equals(str(actualizada.getMotivoConsulta())))
                cambios.put("motivoConsulta", new String[]{str(existente.getMotivoConsulta()), str(actualizada.getMotivoConsulta())});
            if (!str(existente.getSintomas()).equals(str(actualizada.getSintomas())))
                cambios.put("sintomas", new String[]{str(existente.getSintomas()), str(actualizada.getSintomas())});
            if (!str(existente.getDiagnostico()).equals(str(actualizada.getDiagnostico())))
                cambios.put("diagnostico", new String[]{str(existente.getDiagnostico()), str(actualizada.getDiagnostico())});
            if (!str(existente.getTratamiento()).equals(str(actualizada.getTratamiento())))
                cambios.put("tratamiento", new String[]{str(existente.getTratamiento()), str(actualizada.getTratamiento())});
            if (!str(existente.getObservacionesClinicas()).equals(str(actualizada.getObservacionesClinicas())))
                cambios.put("observacionesClinicas", new String[]{str(existente.getObservacionesClinicas()), str(actualizada.getObservacionesClinicas())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("atenciones_clinicas", id, usuario, cambios, "Actualización de atención clínica");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(toResponse(actualizada));
    }

    private AtencionClinicaResponse toResponse(AtencionClinica a) {
        return new AtencionClinicaResponse(a.getId(),
                a.getCita() != null ? a.getCita().getId() : null,
                a.getMascota() != null ? a.getMascota().getId() : null,
                a.getVeterinario() != null ? a.getVeterinario().getId() : null,
                a.getTriaje() != null ? a.getTriaje().getId() : null,
                a.getMotivoConsulta(), a.getSintomas(), a.getDiagnostico(), a.getTratamiento(),
                a.getObservacionesClinicas(),
                a.getCreadoPor() != null ? a.getCreadoPor().getId() : null,
                a.getCreadoEn(),
                a.getActualizadoPor() != null ? a.getActualizadoPor().getId() : null,
                a.getActualizadoEn());
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
