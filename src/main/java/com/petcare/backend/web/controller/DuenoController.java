package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.DuenoService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.ContactoEmergenciaRequest;
import com.petcare.backend.web.dto.request.DuenoRequest;
import com.petcare.backend.web.dto.response.ContactoEmergenciaResponse;
import com.petcare.backend.web.dto.response.DuenoResponse;
import com.petcare.backend.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/duenos")
public class DuenoController {

    private final DuenoService duenoService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public DuenoController(DuenoService duenoService, UsuarioService usuarioService,
                           AuditoriaService auditoriaService) {
        this.duenoService = duenoService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public ResponseEntity<Page<DuenoResponse>> listarDuenos(
            @RequestParam(value = "soloActivos", required = false) Boolean soloActivos,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "dni", required = false) String dni,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DuenoResponse> response = duenoService.listar(soloActivos, nombre, dni, pageable)
                .map(d -> new DuenoResponse(d.getId(), d.getDni(), d.getTelefono(),
                        d.getDireccion(), toUsuarioResponse(d.getUsuario())));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuenoResponse> obtenerDueno(@PathVariable Long id) {
        return duenoService.obtenerPorId(id)
                .map(d -> ResponseEntity.ok(new DuenoResponse(d.getId(), d.getDni(), d.getTelefono(),
                        d.getDireccion(), toUsuarioResponse(d.getUsuario()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<DuenoResponse> registrarDueno(@Valid @RequestBody DuenoRequest request) {
        Usuario usuario = null;
        if (request.userId() != null) {
            usuario = usuarioService.obtenerPorId(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario asociado no encontrado"));
        } else if (request.firstName() != null && request.lastName() != null
                && request.email() != null && request.password() != null) {
            Usuario newUser = Usuario.builder()
                    .contrasena(request.password())
                    .nombres(request.firstName())
                    .apellidos(request.lastName())
                    .email(request.email())
                    .telefono(request.phone())
                    .rol("DUENO")
                    .build();
            usuario = usuarioService.registrarUsuario(newUser);
        }

        Dueno dueno = new Dueno(null, request.dni(), request.phone(), request.address(), usuario);
        Dueno creado = duenoService.registrarDueno(dueno);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String> campos = new java.util.LinkedHashMap<>();
            campos.put("dni", creado.getDni());
            campos.put("telefono", creado.getTelefono());
            campos.put("direccion", creado.getDireccion());
            if (creado.getUsuario() != null) {
                campos.put("usuarioEmail", creado.getUsuario().getEmail());
                campos.put("usuarioNombre", (creado.getUsuario().getNombres() != null ? creado.getUsuario().getNombres() : "") + " " + (creado.getUsuario().getApellidos() != null ? creado.getUsuario().getApellidos() : ""));
            }
            auditoriaService.registrarCreacion("duenos", creado.getId(), auditor, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(new DuenoResponse(creado.getId(), creado.getDni(),
                creado.getTelefono(), creado.getDireccion(),
                toUsuarioResponse(creado.getUsuario())), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<DuenoResponse> actualizarDueno(@PathVariable Long id,
                                                          @Valid @RequestBody DuenoRequest request) {
        Dueno existente = duenoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));
        Dueno detalles = new Dueno(null, request.dni(), request.phone(), request.address(), existente.getUsuario());
        Dueno actualizado = duenoService.actualizarDueno(id, detalles);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String[]> cambios = new java.util.LinkedHashMap<>();
            if (!str(existente.getDni()).equals(str(actualizado.getDni())))
                cambios.put("dni", new String[]{str(existente.getDni()), str(actualizado.getDni())});
            if (!str(existente.getTelefono()).equals(str(actualizado.getTelefono())))
                cambios.put("telefono", new String[]{str(existente.getTelefono()), str(actualizado.getTelefono())});
            if (!str(existente.getDireccion()).equals(str(actualizado.getDireccion())))
                cambios.put("direccion", new String[]{str(existente.getDireccion()), str(actualizado.getDireccion())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("duenos", id, auditor, cambios, "Actualización de dueño");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(new DuenoResponse(actualizado.getId(), actualizado.getDni(),
                actualizado.getTelefono(), actualizado.getDireccion(),
                toUsuarioResponse(actualizado.getUsuario())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarDueno(@PathVariable Long id) {
        Dueno existente = duenoService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));
        duenoService.eliminarDueno(id);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            auditoriaService.registrarEliminacion("duenos", id, auditor, Map.of(
                    "dni", existente.getDni() != null ? existente.getDni() : "",
                    "telefono", existente.getTelefono() != null ? existente.getTelefono() : ""
            ));
        } catch (Exception ignored) { }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> cambiarActivo(@PathVariable Long id) {
        duenoService.toggleActivoDueno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{duenoId}/contactos")
    public ResponseEntity<Page<ContactoEmergenciaResponse>> listarContactos(
            @PathVariable Long duenoId,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "telefono", required = false) String telefono,
            @RequestParam(value = "relacion", required = false) String relacion,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
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

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponse(usuario.getId(), usuario.getNombres(), usuario.getApellidos(),
                usuario.getEmail(), usuario.getTelefono(), usuario.getRol(), usuario.getEstado());
    }

    private Usuario obtenerUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.obtenerPorEmail(username).orElse(null);
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
