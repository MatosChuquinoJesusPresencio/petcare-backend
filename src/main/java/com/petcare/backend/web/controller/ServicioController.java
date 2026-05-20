package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.service.ServicioService;
import com.petcare.backend.web.dto.ServicioRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<Page<Servicio>> listarServicios(
            @RequestParam(value = "soloActivos", defaultValue = "true") boolean soloActivos,
            Pageable pageable) {
        Page<Servicio> servicios = soloActivos ? servicioService.listarActivos(pageable) : servicioService.listarTodos(pageable);
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerServicio(@PathVariable Long id) {
        return servicioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Servicio> crearServicio(@Valid @RequestBody ServicioRequest request) {
        Servicio servicio = Servicio.builder()
                .nombre(request.name())
                .descripcion(request.description())
                .duracionMinutos(request.durationMinutes())
                .costoReferencial(request.referentialCost())
                .build();
        Servicio creado = servicioService.crearServicio(servicio);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @Valid @RequestBody ServicioRequest request) {
        Servicio servicioDetalles = Servicio.builder()
                .nombre(request.name())
                .descripcion(request.description())
                .duracionMinutos(request.durationMinutes())
                .costoReferencial(request.referentialCost())
                .build();
        Servicio actualizado = servicioService.actualizarServicio(id, servicioDetalles);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivarServicio(@PathVariable Long id) {
        servicioService.desactivarServicio(id);
        return ResponseEntity.noContent().build();
    }
}
