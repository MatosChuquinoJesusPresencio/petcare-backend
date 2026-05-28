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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuarioDB.getId());

        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        
        if (rol.startsWith("ROLE_")) {
            rol = rol.substring(5);
        }

        ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(jwt);
        ResponseCookie refreshCookie = jwtUtil.generateRefreshTokenCookie(refreshToken.getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new AuthResponse(jwt, refreshToken.getToken(), request.username(), rol));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(request.password())
                .nombre(request.firstName())
                .apellido(request.lastName())
                .email(request.email())
                .telefono(request.phone())
                .rol(request.role().toUpperCase())
                .build();

        Usuario registrado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
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

        final String finalRefreshToken = requestRefreshToken;

        return refreshTokenService.findByToken(finalRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsuario)
                .map(usuario -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
                    String token = jwtUtil.generateToken(userDetails);
                    ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(token);
                    
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                            .body(new TokenRefreshResponse(token, finalRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database or is invalid."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String requestRefreshToken = jwtUtil.getRefreshTokenFromCookies(request);
        if (requestRefreshToken != null) {
            refreshTokenService.findByToken(requestRefreshToken)
                    .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUsuario().getId()));
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
    public ResponseEntity<AuthResponse> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        if (rol.startsWith("ROLE_")) {
            rol = rol.substring(5);
        }
        return ResponseEntity.ok(new AuthResponse(null, null, username, rol));
    }
}
