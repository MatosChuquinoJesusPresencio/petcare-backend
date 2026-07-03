package com.petcare.backend.domain.model;

import java.math.BigDecimal;

public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionMinutos;
    private BigDecimal costoReferencial;
    private Boolean activo;

    public Servicio() {}

    public Servicio(Long id, String nombre, String descripcion, Integer duracionMinutos, BigDecimal costoReferencial, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionMinutos = duracionMinutos;
        this.costoReferencial = costoReferencial;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public BigDecimal getCostoReferencial() { return costoReferencial; }
    public void setCostoReferencial(BigDecimal costoReferencial) { this.costoReferencial = costoReferencial; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
