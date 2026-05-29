package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.DuenoService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.ContactoEmergenciaRequest;
import com.petcare.backend.web.dto.DuenoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public ResponseEntity<Page<Dueno>> listarDuenos(
            @RequestParam(value = "soloActivos", required = false) Boolean soloActivos,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "dni", required = false) String dni,
            Pageable pageable) {
        Page<Dueno> duenos = duenoService.listar(soloActivos, nombre, dni, pageable);
        return ResponseEntity.ok(duenos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dueno> obtenerDueno(@PathVariable Long id) {
        return duenoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Dueno> registrarDueno(@Valid @RequestBody DuenoRequest request) {
        Usuario usuario = null;
        if (request.userId() != null) {
            usuario = usuarioService.obtenerPorId(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Associated user not found"));
        }

        Dueno dueno = Dueno.builder()
                .nombre(request.firstName())
                .apellido(request.lastName())
                .dni(request.dni())
                .email(request.email())
                .telefono(request.phone())
                .direccion(request.address())
                .usuario(usuario)
                .build();

        Dueno creado = duenoService.registrarDueno(dueno);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Dueno> actualizarDueno(@PathVariable Long id, @Valid @RequestBody DuenoRequest request) {
        Dueno duenoDetalles = Dueno.builder()
                .nombre(request.firstName())
                .apellido(request.lastName())
                .dni(request.dni())
                .email(request.email())
                .telefono(request.phone())
                .direccion(request.address())
                .build();

        Dueno actualizado = duenoService.actualizarDueno(id, duenoDetalles);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarDueno(@PathVariable Long id) {
        duenoService.eliminarDueno(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Dueno> cambiarActivo(@PathVariable Long id) {
        Dueno dueno = duenoService.cambiarActivo(id);
        return ResponseEntity.ok(dueno);
    }

    @GetMapping("/{duenoId}/contactos")
    public ResponseEntity<Page<ContactoEmergencia>> listarContactos(
            @PathVariable Long duenoId,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "relacion", required = false) String relacion,
            Pageable pageable) {
        return ResponseEntity.ok(duenoService.listarContactosDeDueno(duenoId, nombre, telefono, relacion, pageable));
    }

    @PostMapping("/{duenoId}/contactos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<ContactoEmergencia> agregarContacto(@PathVariable Long duenoId, @Valid @RequestBody ContactoEmergenciaRequest request) {
        ContactoEmergencia contacto = ContactoEmergencia.builder()
                .nombre(request.name())
                .telefono(request.phone())
                .relacion(request.relation())
                .build();

        ContactoEmergencia creado = duenoService.agregarContactoEmergencia(duenoId, contacto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @DeleteMapping("/contactos/{contactoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarContacto(@PathVariable Long contactoId) {
        duenoService.eliminarContactoEmergencia(contactoId);
        return ResponseEntity.noContent().build();
    }
}
