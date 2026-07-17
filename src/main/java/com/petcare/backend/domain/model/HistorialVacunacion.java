package com.petcare.backend.domain.model;

import java.time.Instant;
import java.time.LocalDate;

public class HistorialVacunacion {
    private Long id;
    private Mascota mascota;
    private String tipo;
    private String nombreProducto;
    private LocalDate fechaAplicacion;
    private LocalDate proximaDosis;
    private String lote;
    private String fabricante;
    private String dosis;
    private String viaAdministracion;
    private Usuario veterinario;
    private String observaciones;
    private String estado;
    private Usuario creadoPor;
    private Instant creadoEn;
    private Usuario actualizadoPor;
    private Instant actualizadoEn;

    public HistorialVacunacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Mascota getMascota() { return mascota; }
    public String getTipo() { return tipo; }
    public String getNombreProducto() { return nombreProducto; }
    public LocalDate getFechaAplicacion() { return fechaAplicacion; }
    public LocalDate getProximaDosis() { return proximaDosis; }
    public String getLote() { return lote; }
    public String getFabricante() { return fabricante; }
    public String getDosis() { return dosis; }
    public String getViaAdministracion() { return viaAdministracion; }
    public Usuario getVeterinario() { return veterinario; }
    public String getObservaciones() { return observaciones; }
    public String getEstado() { return estado; }
    public Usuario getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public Usuario getActualizadoPor() { return actualizadoPor; }
    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public void setFechaAplicacion(LocalDate fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
    public void setProximaDosis(LocalDate proximaDosis) { this.proximaDosis = proximaDosis; }
    public void setLote(String lote) { this.lote = lote; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public void setViaAdministracion(String viaAdministracion) { this.viaAdministracion = viaAdministracion; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public void setActualizadoPor(Usuario actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
