package com.petcare.backend.domain.model;

public class Usuario {
    private Long id;
    private String contrasena;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private String rol;
    private Boolean estado;

    public Usuario() {}

    public Usuario(
        Long id,
        String contrasena, 
        String nombres, 
        String apellidos, 
        String email,
        String telefono, 
        String rol, 
        Boolean estado
    ) {
        this.id = id;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public String getContrasena() { return contrasena; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getRol() { return rol; }
    public Boolean getEstado() { return estado; }

    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
