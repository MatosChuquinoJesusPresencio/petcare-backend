package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.web.dto.UsuarioRequest;
import com.petcare.backend.web.dto.UsuarioResponse;
import com.petcare.backend.web.dto.UsuarioUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
            @RequestParam(value = "soloActivos", required = false) Boolean soloActivos,
            @RequestParam(value = "rol", required = false) String rol,
            Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listar(soloActivos, rol, pageable).map(this::toResponse));
    }

    @GetMapping("/veterinarios")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<List<UsuarioResponse>> listarVeterinariosActivos() {
        return ResponseEntity.ok(usuarioService.listarVeterinariosActivos().stream()
                .map(this::toResponse)
                .toList());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(request.password())
                .nombre(request.firstName())
                .apellido(request.lastName())
                .email(request.email())
                .telefono(request.phone())
                .rol(request.role())
                .build();

        Usuario creado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(toResponse(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id,
                                                             @Valid @RequestBody UsuarioUpdateRequest request) {
        Usuario usuarioDetalles = Usuario.builder()
                .username(request.username())
                .password(request.password())
                .nombre(request.firstName())
                .apellido(request.lastName())
                .email(request.email())
                .telefono(request.phone())
                .rol(request.role())
                .build();

        Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioDetalles);
        return ResponseEntity.ok(toResponse(actualizado));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<UsuarioResponse> cambiarActivo(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(usuarioService.cambiarActivo(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRol(),
                usuario.getActivo()
        );
    }
}
