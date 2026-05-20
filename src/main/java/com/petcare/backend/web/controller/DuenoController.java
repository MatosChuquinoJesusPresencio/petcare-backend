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

import java.util.List;

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
    public ResponseEntity<List<Dueno>> listarDuenos() {
        return ResponseEntity.ok(duenoService.listarTodos());
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
        if (request.usuarioId() != null) {
            usuario = usuarioService.obtenerPorId(request.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario asociado no encontrado"));
        }

        Dueno dueno = Dueno.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .dni(request.dni())
                .email(request.email())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .usuario(usuario)
                .build();

        Dueno creado = duenoService.registrarDueno(dueno);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Dueno> actualizarDueno(@PathVariable Long id, @Valid @RequestBody DuenoRequest request) {
        Dueno duenoDetalles = Dueno.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .dni(request.dni())
                .email(request.email())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .build();

        Dueno actualizado = duenoService.actualizarDueno(id, duenoDetalles);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> desactivarDueno(@PathVariable Long id) {
        duenoService.desactivarDueno(id);
        return ResponseEntity.noContent().build();
    }

    // Contactos de emergencia
    @GetMapping("/{duenoId}/contactos")
    public ResponseEntity<List<ContactoEmergencia>> listarContactos(@PathVariable Long duenoId) {
        return ResponseEntity.ok(duenoService.listarContactosDeDueno(duenoId));
    }

    @PostMapping("/{duenoId}/contactos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<ContactoEmergencia> agregarContacto(@PathVariable Long duenoId, @Valid @RequestBody ContactoEmergenciaRequest request) {
        ContactoEmergencia contacto = ContactoEmergencia.builder()
                .nombre(request.nombre())
                .telefono(request.telefono())
                .relacion(request.relacion())
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
