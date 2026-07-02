package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.TriajeService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.TriajeRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/triajes")
public class TriajeController {

    private final TriajeService triajeService;
    private final UsuarioService usuarioService;

    public TriajeController(TriajeService triajeService, UsuarioService usuarioService) {
        this.triajeService = triajeService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Triaje> crearTriaje(@Valid @RequestBody TriajeRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario asistente = usuarioService.obtenerPorUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        Triaje triaje = Triaje.builder()
                .motivoVisita(request.motivoVisita())
                .nivelUrgencia(request.nivelUrgencia())
                .signosVisibles(request.signosVisibles())
                .observaciones(request.observaciones())
                .peso(request.peso())
                .temperatura(request.temperatura())
                .frecuenciaCardiaca(request.frecuenciaCardiaca())
                .frecuenciaRespiratoria(request.frecuenciaRespiratoria())
                .build();

        Triaje creado = triajeService.crearTriaje(triaje, request.citaId(), asistente.getId());
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Triaje>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(triajeService.listarTodos(pageable));
    }

    @GetMapping("/prioridad/{nivelUrgencia}")
    public ResponseEntity<Page<Triaje>> listarPorPrioridad(@PathVariable String nivelUrgencia, Pageable pageable) {
        return ResponseEntity.ok(triajeService.listarPorPrioridad(nivelUrgencia, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Triaje> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(triajeService.obtenerPorId(id));
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<Triaje> obtenerPorCitaId(@PathVariable Long citaId) {
        return ResponseEntity.ok(triajeService.obtenerPorCitaId(citaId));
    }
}
