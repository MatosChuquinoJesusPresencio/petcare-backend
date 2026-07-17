package com.petcare.backend.web.controller;

import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.web.dto.request.ActualizarUsuarioRequest;
import com.petcare.backend.web.dto.request.CambiarContrasenaRequest;
import com.petcare.backend.web.dto.request.RegisterRequest;
import com.petcare.backend.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public UsuarioController(UsuarioService usuarioService, AuditoriaService auditoriaService) {
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<UsuarioResponse>> listarTodos(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarTodos(pageable)
                .map(u -> new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                        u.getEmail(), u.getTelefono(), u.getRol(), u.getEstado())));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        var usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new com.petcare.backend.domain.exception.ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(new UsuarioResponse(usuario.getId(), usuario.getNombres(),
                usuario.getApellidos(), usuario.getEmail(), usuario.getTelefono(),
                usuario.getRol(), usuario.getEstado()));
    }

    @GetMapping("/veterinarios")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<Page<UsuarioResponse>> listarVeterinariosActivos(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarVeterinariosActivos(pageable)
                .map(u -> new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                        u.getEmail(), u.getTelefono(), u.getRol(), u.getEstado())));
    }

    @GetMapping("/veterinarios/todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Page<UsuarioResponse>> listarTodosVeterinarios(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarVeterinarios(pageable)
                .map(u -> new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                        u.getEmail(), u.getTelefono(), u.getRol(), u.getEstado())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id,
                                                             @Valid @RequestBody ActualizarUsuarioRequest request) {
        Usuario existente = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new com.petcare.backend.domain.exception.ResourceNotFoundException("Usuario no encontrado"));

        var usuario = usuarioService.actualizarUsuario(id, request.firstName(), request.lastName(),
                request.email(), request.phone(), request.role().toUpperCase());

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            Map<String, String[]> cambios = new HashMap<>();
            if (!str(existente.getNombres()).equals(request.firstName()))
                cambios.put("nombres", new String[]{existente.getNombres(), request.firstName()});
            if (!str(existente.getApellidos()).equals(request.lastName()))
                cambios.put("apellidos", new String[]{existente.getApellidos(), request.lastName()});
            if (!str(existente.getEmail()).equals(request.email()))
                cambios.put("email", new String[]{existente.getEmail(), request.email()});
            if (!str(existente.getTelefono()).equals(request.phone()))
                cambios.put("telefono", new String[]{existente.getTelefono(), request.phone()});
            if (!str(existente.getRol()).equals(request.role().toUpperCase()))
                cambios.put("rol", new String[]{existente.getRol(), request.role().toUpperCase()});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("usuarios", id, auditor, cambios, "Edicion de usuario");
            }
        } catch (Exception ignored) { }

        return ResponseEntity.ok(new UsuarioResponse(usuario.getId(), usuario.getNombres(),
                usuario.getApellidos(), usuario.getEmail(), usuario.getTelefono(),
                usuario.getRol(), usuario.getEstado()));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> cambiarEstado(@PathVariable Long id,
                                                           @RequestBody Map<String, Boolean> body) {
        Boolean estado = body.get("active");
        if (estado == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario existente = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new com.petcare.backend.domain.exception.ResourceNotFoundException("Usuario no encontrado"));
        Boolean estadoAnterior = existente.getEstado();

        var usuario = usuarioService.cambiarEstado(id, estado);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            Map<String, String[]> cambios = Map.of("estado",
                    new String[]{String.valueOf(estadoAnterior), String.valueOf(estado)});
            String motivo = estado ? "Habilitacion de usuario" : "Deshabilitacion de usuario";
            auditoriaService.registrarEdicion("usuarios", id, auditor, cambios, motivo);
        } catch (Exception ignored) { }

        return ResponseEntity.ok(new UsuarioResponse(usuario.getId(), usuario.getNombres(),
                usuario.getApellidos(), usuario.getEmail(), usuario.getTelefono(),
                usuario.getRol(), usuario.getEstado()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody RegisterRequest request) {
        com.petcare.backend.domain.model.Usuario usuario = com.petcare.backend.domain.model.Usuario.builder()
                .contrasena(request.password())
                .nombres(request.firstName())
                .apellidos(request.lastName())
                .email(request.email())
                .telefono(request.phone())
                .rol(request.role().toUpperCase())
                .build();
        var creado = usuarioService.registrarUsuario(usuario);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            Map<String, String> campos = new HashMap<>();
            campos.put("nombres", creado.getNombres());
            campos.put("apellidos", creado.getApellidos());
            campos.put("email", creado.getEmail());
            campos.put("telefono", creado.getTelefono());
            campos.put("rol", creado.getRol());
            auditoriaService.registrarCreacion("usuarios", creado.getId(), auditor, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(new UsuarioResponse(creado.getId(), creado.getNombres(),
                creado.getApellidos(), creado.getEmail(), creado.getTelefono(),
                creado.getRol(), creado.getEstado()), HttpStatus.CREATED);
    }

    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<Void> cambiarContrasena(Authentication authentication,
                                                   @Valid @RequestBody CambiarContrasenaRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BusinessRuleException("Las contraseñas no coinciden");
        }
        var usuario = usuarioService.obtenerPorEmail(authentication.getName())
                .orElseThrow(() -> new com.petcare.backend.domain.exception.ResourceNotFoundException("Usuario no encontrado"));
        usuarioService.cambiarContrasena(usuario.getId(), request.currentPassword(), request.newPassword());

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            Map<String, String> campos = Map.of("accion", "Cambio de contrasena", "usuario", usuario.getEmail());
            auditoriaService.registrarEdicion("usuarios", usuario.getId(), auditor,
                    Map.of("contrasena", new String[]{"***", "***"}), "Cambio de contrasena propio");
        } catch (Exception ignored) { }

        return ResponseEntity.noContent().build();
    }

    private Usuario obtenerUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.obtenerPorEmail(username).orElse(null);
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
