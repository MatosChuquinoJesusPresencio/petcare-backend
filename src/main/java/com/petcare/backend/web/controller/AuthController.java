package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.RefreshTokenService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.TokenRefreshException;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.web.dto.response.AuthResponse;
import com.petcare.backend.web.dto.response.TokenRefreshResponse;
import com.petcare.backend.web.dto.request.LoginRequest;
import com.petcare.backend.web.dto.request.RegisterRequest;
import com.petcare.backend.web.dto.request.TokenRefreshRequest;
import com.petcare.backend.web.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioService usuarioService,
                          RefreshTokenService refreshTokenService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuarioDB = usuarioService.obtenerPorEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        usuarioService.incrementarTokenVersion(usuarioDB.getId());

        String jwt = jwtUtil.generateToken(usuarioDB);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuarioDB.getId());

        String rol = usuarioDB.getRol();

        ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(jwt);
        ResponseCookie refreshCookie = jwtUtil.generateRefreshTokenCookie(refreshToken.getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new AuthResponse(usuarioDB.getId(), request.email(), rol));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .contrasena(request.password())
                .nombres(request.firstName())
                .apellidos(request.lastName())
                .email(request.email())
                .telefono(request.phone())
                .rol(request.role().toUpperCase())
                .tokenVersion(0)
                .build();

        Usuario registrado = usuarioService.registrarUsuario(usuario);

        String jwt = jwtUtil.generateToken(registrado);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(registrado.getId());

        ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(jwt);
        ResponseCookie refreshCookie = jwtUtil.generateRefreshTokenCookie(refreshToken.getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        String rol = registrado.getRol();
        return new ResponseEntity<>(new AuthResponse(registrado.getId(), registrado.getEmail(), rol), headers, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(
            HttpServletRequest request,
            @Valid @RequestBody(required = false) TokenRefreshRequest bodyRequest) {

        String requestRefreshToken = jwtUtil.getRefreshTokenFromCookies(request);

        if (requestRefreshToken == null && bodyRequest != null) {
            requestRefreshToken = bodyRequest.refreshToken();
        }

        if (requestRefreshToken == null || requestRefreshToken.trim().isEmpty()) {
            throw new TokenRefreshException("Refresh token is not present in cookies or request body.");
        }

        String finalRefreshToken = requestRefreshToken;

        return refreshTokenService.findByToken(finalRefreshToken)
                .map(token -> {
                    refreshTokenService.verifyExpirationAndDelete(token);
                    Usuario usuario = token.getUsuario();
                    if (usuario == null) {
                        throw new TokenRefreshException("User not found for refresh token");
                    }

                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(usuario.getId());

                    String jwt = jwtUtil.generateToken(usuario);
                    ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(jwt);
                    ResponseCookie newRefreshCookie = jwtUtil.generateRefreshTokenCookie(newRefreshToken.getToken());

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                            .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                            .body(new TokenRefreshResponse(jwt));
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database or is invalid."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String requestRefreshToken = jwtUtil.getRefreshTokenFromCookies(request);
        if (requestRefreshToken != null) {
            refreshTokenService.findByToken(requestRefreshToken).ifPresent(token -> {
                refreshTokenService.deleteByToken(token.getToken());
            });
        }

        ResponseCookie cleanJwtCookie = jwtUtil.getCleanJwtCookie();
        ResponseCookie cleanRefreshCookie = jwtUtil.getCleanRefreshTokenCookie();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cleanJwtCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString());

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        var authorities = authentication.getAuthorities();
        String rol = authorities.isEmpty() ? "UNKNOWN" : authorities.iterator().next().getAuthority();
        if (rol.startsWith("ROLE_")) {
            rol = rol.substring(5);
        }
        Usuario usuario = usuarioService.obtenerPorEmail(username)
                .orElse(null);
        Long id = usuario != null ? usuario.getId() : null;
        return ResponseEntity.ok(new AuthResponse(id, username, rol));
    }
}
