package com.petcare.backend.domain.model;

import java.time.Instant;

public class Cita {
    private Long id;
    private Mascota mascota;
    private Usuario veterinario;
    private Servicio servicio;
    private Instant fechaHora;
    private String estado;
    private String notas;
    private Usuario creadoPor;
    private Instant creadoEn;
    private Usuario actualizadoPor;
    private Instant actualizadoEn;

    public Cita() {}

    public Cita(Long id, Mascota mascota, Usuario veterinario, Servicio servicio, Instant fechaHora, String estado, String notas, Usuario creadoPor, Instant creadoEn, Usuario actualizadoPor, Instant actualizadoEn) {
        this.id = id;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.notas = notas;
        this.creadoPor = creadoPor;
        this.creadoEn = creadoEn;
        this.actualizadoPor = actualizadoPor;
        this.actualizadoEn = actualizadoEn;
    }

    public Long getId() { return id; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Usuario getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public Usuario getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(Usuario actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
