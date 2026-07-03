package com.petcare.backend.domain.model;

public class Dueno {
    private Long id;
    private String dni;
    private String telefono;
    private String direccion;
    private Usuario usuario;

    public Dueno() {}

    public Dueno(
        Long id, 
        String dni, 
        String telefono,
        String direccion, 
        Usuario usuario
    ) {
        this.id = id;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public Usuario getUsuario() { return usuario; }
    
    public void setDni(String dni) { this.dni = dni; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
