package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.RefreshTokenService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.TokenRefreshException;
import com.petcare.backend.web.dto.AuthResponse;
import com.petcare.backend.web.dto.LoginRequest;
import com.petcare.backend.web.dto.RegisterRequest;
import com.petcare.backend.web.dto.TokenRefreshRequest;
import com.petcare.backend.web.dto.TokenRefreshResponse;
import com.petcare.backend.web.security.CustomUserDetailsService;
import com.petcare.backend.web.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioService usuarioService,
                          RefreshTokenService refreshTokenService,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        final String jwt = jwtUtil.generateToken(userDetails);

        Usuario usuarioDB = usuarioService.obtenerPorUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuarioDB.getId());

        // Obtener rol
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        // Quitar "ROLE_" si lo tiene
        if (rol.startsWith("ROLE_")) {
            rol = rol.substring(5);
        }

        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken.getToken(), request.username(), rol));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(request.password())
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .telefono(request.telefono())
                .rol(request.rol().toUpperCase())
                .build();

        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsuario)
                .map(usuario -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
                    String token = jwtUtil.generateToken(userDetails);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("El Refresh token no se encuentra en la base de datos o es inválido."));
    }
}
