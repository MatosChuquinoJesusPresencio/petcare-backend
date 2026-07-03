package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.service.ServicioService;
import com.petcare.backend.web.dto.request.ServicioRequest;
import com.petcare.backend.web.dto.response.ServicioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<Page<ServicioResponse>> listarServicios(
            @RequestParam(value = "soloActivos", required = false) Boolean soloActivos,
            @RequestParam(value = "nombre", required = false) String nombre,
            Pageable pageable) {
        Page<ServicioResponse> servicios = servicioService.listar(soloActivos, nombre, pageable)
                .map(s -> new ServicioResponse(s.getId(), s.getNombre(), s.getDescripcion(),
                        s.getDuracionMinutos(), s.getCostoReferencial(), s.getActivo()));
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> obtenerServicio(@PathVariable Long id) {
        return servicioService.obtenerPorId(id)
                .map(s -> ResponseEntity.ok(new ServicioResponse(s.getId(), s.getNombre(),
                        s.getDescripcion(), s.getDuracionMinutos(), s.getCostoReferencial(), s.getActivo())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ServicioResponse> crearServicio(@Valid @RequestBody ServicioRequest request) {
        Servicio servicio = new Servicio(null, request.name(), request.description(),
                request.durationMinutes(), request.referentialCost(), true);
        Servicio creado = servicioService.crearServicio(servicio);
        return new ResponseEntity<>(new ServicioResponse(creado.getId(), creado.getNombre(),
                creado.getDescripcion(), creado.getDuracionMinutos(), creado.getCostoReferencial(),
                creado.getActivo()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ServicioResponse> actualizarServicio(@PathVariable Long id,
                                                                @Valid @RequestBody ServicioRequest request) {
        Servicio detalles = new Servicio(null, request.name(), request.description(),
                request.durationMinutes(), request.referentialCost(), null);
        Servicio actualizado = servicioService.actualizarServicio(id, detalles);
        return ResponseEntity.ok(new ServicioResponse(actualizado.getId(), actualizado.getNombre(),
                actualizado.getDescripcion(), actualizado.getDuracionMinutos(),
                actualizado.getCostoReferencial(), actualizado.getActivo()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ServicioResponse> cambiarActivo(@PathVariable Long id) {
        Servicio servicio = servicioService.cambiarActivo(id);
        return ResponseEntity.ok(new ServicioResponse(servicio.getId(), servicio.getNombre(),
                servicio.getDescripcion(), servicio.getDuracionMinutos(),
                servicio.getCostoReferencial(), servicio.getActivo()));
    }
}
