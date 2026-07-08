package com.petcare.backend.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    private Long id;
    private String contrasena;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String rol;
    private Boolean estado;
    @Builder.Default
    private Integer tokenVersion = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContrasena() { return contrasena; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
    public Boolean getEstado() { return estado; }
    public Integer getTokenVersion() { return tokenVersion; }

    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public void setTokenVersion(Integer tokenVersion) { this.tokenVersion = tokenVersion; }
}
