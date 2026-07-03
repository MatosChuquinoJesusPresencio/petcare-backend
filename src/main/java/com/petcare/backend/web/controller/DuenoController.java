package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.DuenoService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.ContactoEmergenciaRequest;
import com.petcare.backend.web.dto.request.DuenoRequest;
import com.petcare.backend.web.dto.response.ContactoEmergenciaResponse;
import com.petcare.backend.web.dto.response.DuenoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/duenos")
public class DuenoController {

    private final DuenoService duenoService;
    private final UsuarioService usuarioService;

    public DuenoController(DuenoService duenoService, UsuarioService usuarioService) {
        this.duenoService = duenoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Page<DuenoResponse>> listarDuenos(
            @RequestParam(value = "soloActivos", required = false) Boolean soloActivos,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "dni", required = false) String dni,
            Pageable pageable) {
        Page<DuenoResponse> response = duenoService.listar(soloActivos, nombre, dni, pageable)
                .map(d -> new DuenoResponse(d.getId(), d.getDni(), d.getTelefono(),
                        d.getDireccion(), d.getUsuario() != null ? d.getUsuario().getId() : null));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuenoResponse> obtenerDueno(@PathVariable Long id) {
        return duenoService.obtenerPorId(id)
                .map(d -> ResponseEntity.ok(new DuenoResponse(d.getId(), d.getDni(), d.getTelefono(),
                        d.getDireccion(), d.getUsuario() != null ? d.getUsuario().getId() : null)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<DuenoResponse> registrarDueno(@Valid @RequestBody DuenoRequest request) {
        Usuario usuario = null;
        if (request.userId() != null) {
            usuario = usuarioService.obtenerPorId(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Associated user not found"));
        }

        Dueno dueno = new Dueno(null, request.dni(), request.phone(), request.address(), usuario);
        Dueno creado = duenoService.registrarDueno(dueno);
        return new ResponseEntity<>(new DuenoResponse(creado.getId(), creado.getDni(),
                creado.getTelefono(), creado.getDireccion(),
                creado.getUsuario() != null ? creado.getUsuario().getId() : null), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<DuenoResponse> actualizarDueno(@PathVariable Long id,
                                                          @Valid @RequestBody DuenoRequest request) {
        Dueno existente = duenoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Dueno detalles = new Dueno(null, request.dni(), request.phone(), request.address(), existente.getUsuario());
        Dueno actualizado = duenoService.actualizarDueno(id, detalles);
        return ResponseEntity.ok(new DuenoResponse(actualizado.getId(), actualizado.getDni(),
                actualizado.getTelefono(), actualizado.getDireccion(),
                actualizado.getUsuario() != null ? actualizado.getUsuario().getId() : null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarDueno(@PathVariable Long id) {
        duenoService.eliminarDueno(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> cambiarActivo(@PathVariable Long id) {
        duenoService.desactivarDueno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{duenoId}/contactos")
    public ResponseEntity<Page<ContactoEmergenciaResponse>> listarContactos(
            @PathVariable Long duenoId,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "relacion", required = false) String relacion,
            Pageable pageable) {
        Page<ContactoEmergenciaResponse> response = duenoService
                .listarContactosDeDueno(duenoId, nombre, telefono, relacion, pageable)
                .map(c -> new ContactoEmergenciaResponse(c.getId(),
                        c.getDueno() != null ? c.getDueno().getId() : null,
                        c.getNombre(), c.getTelefono(), c.getRelacion()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{duenoId}/contactos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<ContactoEmergenciaResponse> agregarContacto(@PathVariable Long duenoId,
                                                                       @Valid @RequestBody ContactoEmergenciaRequest request) {
        ContactoEmergencia contacto = new ContactoEmergencia(null, null, request.name(),
                request.phone(), request.relation());
        ContactoEmergencia creado = duenoService.agregarContactoEmergencia(duenoId, contacto);
        return new ResponseEntity<>(new ContactoEmergenciaResponse(creado.getId(),
                creado.getDueno() != null ? creado.getDueno().getId() : null, creado.getNombre(), creado.getTelefono(),
                creado.getRelacion()), HttpStatus.CREATED);
    }

    @DeleteMapping("/contactos/{contactoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarContacto(@PathVariable Long contactoId) {
        duenoService.eliminarContactoEmergencia(contactoId);
        return ResponseEntity.noContent().build();
    }
}
