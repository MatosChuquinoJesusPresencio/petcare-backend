package com.petcare.backend.web.security;

import com.petcare.backend.domain.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final JwtTokenProvider jwtTokenProvider;
    private final long jwtExpirationMs;
    private final long refreshExpirationMs;
    private final String cookieDomain;

    public JwtUtil(JwtTokenProvider jwtTokenProvider,
                   @Value("${jwt.expiration}") long jwtExpirationMs,
                   @Value("${jwt.refreshExpirationMs}") long refreshExpirationMs,
                   @Value("${app.cookie.domain:}") String cookieDomain) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtExpirationMs = jwtExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
        this.cookieDomain = cookieDomain;
    }

    public String generateToken(UserDetails userDetails) {
        String rol = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        Usuario usuario = new Usuario();
        usuario.setEmail(userDetails.getUsername());
        usuario.setRol(rol);
        return jwtTokenProvider.generateAccessToken(usuario);
    }

    public String generateToken(Usuario usuario) {
        return jwtTokenProvider.generateAccessToken(usuario);
    }

    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .domain(cookieDomain.isBlank() ? null : cookieDomain)
                .build();
    }

    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(refreshExpirationMs / 1000)
                .domain(cookieDomain.isBlank() ? null : cookieDomain)
                .build();
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();
    }

    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();
    }
}
