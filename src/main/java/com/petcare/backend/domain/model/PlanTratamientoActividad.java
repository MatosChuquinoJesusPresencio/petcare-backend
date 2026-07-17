package com.petcare.backend.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class PlanTratamientoActividad {
    private Long id;
    private String tipo;
    private String descripcion;
    private LocalDate fechaProgramada;
    private LocalTime horaProgramada;
    private String frecuencia;
    private String responsable;
    private String estado;
    private String observaciones;

    public PlanTratamientoActividad() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaProgramada() { return fechaProgramada; }
    public LocalTime getHoraProgramada() { return horaProgramada; }
    public String getFrecuencia() { return frecuencia; }
    public String getResponsable() { return responsable; }
    public String getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaProgramada(LocalDate fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    public void setHoraProgramada(LocalTime horaProgramada) { this.horaProgramada = horaProgramada; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
