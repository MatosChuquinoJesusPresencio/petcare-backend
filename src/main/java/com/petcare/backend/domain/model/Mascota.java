package com.petcare.backend.domain.model;

import java.time.LocalDate;

public class Mascota {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private String genero;
    private LocalDate fechaNacimiento;
    private String microchip;
    private String condicionReproductiva;
    private String alergias;
    private String enfermedadesCronicas;
    private String alertasMedicas;
    private String notasMedicas;
    private Boolean estado;

    public Mascota() {}

    public Mascota(Long id, String nombre, String especie, String raza, String genero, LocalDate fechaNacimiento,
            String microchip, String condicionReproductiva, String alergias, String enfermedadesCronicas,
            String alertasMedicas, String notasMedicas, Boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.microchip = microchip;
        this.condicionReproductiva = condicionReproductiva;
        this.alergias = alergias;
        this.enfermedadesCronicas = enfermedadesCronicas;
        this.alertasMedicas = alertasMedicas;
        this.notasMedicas = notasMedicas;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getRaza() { return raza; }
    public String getGenero() { return genero; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getMicrochip() { return microchip; }
    public String getCondicionReproductiva() { return condicionReproductiva; }
    public String getAlergias() { return alergias; }
    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public String getAlertasMedicas() { return alertasMedicas; }
    public String getNotasMedicas() { return notasMedicas; }
    public Boolean getEstado() { return estado; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setRaza(String raza) { this.raza = raza; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setMicrochip(String microchip) { this.microchip = microchip; }
    public void setCondicionReproductiva(String condicionReproductiva) { this.condicionReproductiva = condicionReproductiva; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }
    public void setAlertasMedicas(String alertasMedicas) { this.alertasMedicas = alertasMedicas; }
    public void setNotasMedicas(String notasMedicas) { this.notasMedicas = notasMedicas; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
