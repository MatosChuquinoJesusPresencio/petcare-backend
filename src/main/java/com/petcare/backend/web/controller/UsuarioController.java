package com.petcare.backend.web.controller;

import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.web.dto.request.RegisterRequest;
import com.petcare.backend.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/veterinarios")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<List<UsuarioResponse>> listarVeterinariosActivos() {
        var usuarios = usuarioService.listarVeterinariosActivos().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                        u.getEmail(), u.getTelefono(), u.getRol(), u.getEstado()))
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/veterinarios/todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<List<UsuarioResponse>> listarTodosVeterinarios() {
        var usuarios = usuarioService.listarVeterinarios().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                        u.getEmail(), u.getTelefono(), u.getRol(), u.getEstado()))
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> cambiarEstado(@PathVariable Long id,
                                                           @RequestBody Map<String, Boolean> body) {
        Boolean estado = body.get("active");
        if (estado == null) {
            return ResponseEntity.badRequest().build();
        }
        var usuario = usuarioService.cambiarEstado(id, estado);
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
        return new ResponseEntity<>(new UsuarioResponse(creado.getId(), creado.getNombres(),
                creado.getApellidos(), creado.getEmail(), creado.getTelefono(),
                creado.getRol(), creado.getEstado()), HttpStatus.CREATED);
    }
}
