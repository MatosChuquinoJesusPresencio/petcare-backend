package com.petcare.backend.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanTratamiento {
    private Long id;
    private Mascota mascota;
    private Long atencionClinicaId;
    private Usuario veterinario;
    private String titulo;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private String estado;
    private Usuario creadoPor;
    private Instant creadoEn;
    private List<PlanTratamientoActividad> actividades;

    public PlanTratamiento() { this.actividades = new ArrayList<>(); }

    public Long getId() { return id; }
    public Mascota getMascota() { return mascota; }
    public Long getAtencionClinicaId() { return atencionClinicaId; }
    public Usuario getVeterinario() { return veterinario; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public String getEstado() { return estado; }
    public Usuario getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public List<PlanTratamientoActividad> getActividades() { return actividades; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public void setAtencionClinicaId(Long atencionClinicaId) { this.atencionClinicaId = atencionClinicaId; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public void setActividades(List<PlanTratamientoActividad> actividades) { this.actividades = actividades; }
}
