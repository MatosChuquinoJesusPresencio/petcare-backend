package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.BloqueoVeterinarioService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.BloqueoRequest;
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
    public ResponseEntity<List<BloqueoVeterinario>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(bloqueoService.listarPorVeterinario(veterinarioId));
    }

    @GetMapping("/veterinario/{veterinarioId}/fecha")
    public ResponseEntity<List<BloqueoVeterinario>> listarPorVeterinarioYFecha(
            @PathVariable Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(bloqueoService.listarPorVeterinarioYFecha(veterinarioId, fecha));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloqueoVeterinario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bloqueoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<BloqueoVeterinario> bloquear(@Valid @RequestBody BloqueoRequest request) {
        Usuario veterinario = usuarioService.obtenerPorId(request.veterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        BloqueoVeterinario bloqueo = BloqueoVeterinario.builder()
                .veterinario(veterinario)
                .fecha(request.fecha())
                .horaInicio(request.horaInicio())
                .horaFin(request.horaFin())
                .motivo(request.motivo())
                .build();

        BloqueoVeterinario creado = bloqueoService.bloquear(bloqueo);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VETERINARIO')")
    public ResponseEntity<Void> eliminarBloqueo(@PathVariable Long id) {
        bloqueoService.eliminarBloqueo(id);
        return ResponseEntity.noContent().build();
    }
}
