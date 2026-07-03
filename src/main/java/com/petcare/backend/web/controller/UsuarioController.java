package com.petcare.backend.web.controller;

import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.web.dto.response.UsuarioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/veterinarios")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE', 'VETERINARIO')")
    public ResponseEntity<List<UsuarioResponse>> listarVeterinariosActivos() {
        var usuarios = usuarioService.listarVeterinariosActivos().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombres(), u.getApellidos(),
                        u.getEmail(), u.getTelefono(), u.getRol(), u.getEstado()))
                .toList();
        return ResponseEntity.ok(usuarios);
    }
}
