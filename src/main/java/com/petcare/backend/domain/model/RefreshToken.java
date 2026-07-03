package com.petcare.backend.domain.model;

import java.time.Instant;

public class RefreshToken {
    private Long id;
    private Usuario usuario;
    private String token;
    private Instant fechaExpiracion;

    public RefreshToken() {}

    public RefreshToken(Long id, Usuario usuario, String token, Instant fechaExpiracion) {
        this.id = id;
        this.usuario = usuario;
        this.token = token;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Instant getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(Instant fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}
