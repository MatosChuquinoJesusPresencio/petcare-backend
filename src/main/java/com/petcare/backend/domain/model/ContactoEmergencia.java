package com.petcare.backend.domain.model;

public class ContactoEmergencia {
    private Long id;
    private Dueno dueno;
    private String nombre;
    private String telefono;
    private String relacion;

    public ContactoEmergencia() {}

    public ContactoEmergencia(Long id, Dueno dueno, String nombre, String telefono, String relacion) {
        this.id = id;
        this.dueno = dueno;
        this.nombre = nombre;
        this.telefono = telefono;
        this.relacion = relacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Dueno getDueno() { return dueno; }
    public void setDueno(Dueno dueno) { this.dueno = dueno; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getRelacion() { return relacion; }
    public void setRelacion(String relacion) { this.relacion = relacion; }
}
