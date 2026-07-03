package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.BloqueoVeterinarioService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.BloqueoRequest;
import com.petcare.backend.web.dto.response.BloqueoVeterinarioResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bloqueos")
public class BloqueoVeterinarioController {

    private final BloqueoVeterinarioService bloqueoService;
    private final UsuarioService usuarioService;

    public BloqueoVeterinarioController(BloqueoVeterinarioService bloqueoService,
                                         UsuarioService usuarioService) {
        this.bloqueoService = bloqueoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<List<BloqueoVeterinarioResponse>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(toResponseList(bloqueoService.listarPorVeterinario(veterinarioId)));
    }

    @GetMapping("/veterinario/{veterinarioId}/fecha")
    public ResponseEntity<List<BloqueoVeterinarioResponse>> listarPorVeterinarioYFecha(
            @PathVariable Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(toResponseList(bloqueoService.listarPorVeterinarioYFecha(veterinarioId, fecha)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloqueoVeterinarioResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(bloqueoService.obtenerPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<BloqueoVeterinarioResponse> bloquear(@Valid @RequestBody BloqueoRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarianId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        BloqueoVeterinario bloqueo = new BloqueoVeterinario(null, veterinario, request.date(),
                request.startTime(), request.endTime(), request.reason());

        BloqueoVeterinario creado = bloqueoService.bloquear(bloqueo);
        return new ResponseEntity<>(toResponse(creado), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Void> eliminarBloqueo(@PathVariable Long id) {
        bloqueoService.eliminarBloqueo(id);
        return ResponseEntity.noContent().build();
    }

    private BloqueoVeterinarioResponse toResponse(BloqueoVeterinario b) {
        return new BloqueoVeterinarioResponse(b.getId(), b.getVeterinario().getId(), b.getFecha(),
                b.getHoraInicio(), b.getHoraFin(), b.getMotivo());
    }

    private List<BloqueoVeterinarioResponse> toResponseList(List<BloqueoVeterinario> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
