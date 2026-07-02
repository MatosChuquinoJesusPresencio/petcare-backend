package com.petcare.backend.domain.model;

import java.time.Instant;

public class BloqueoVeterinario {
    private Long id;
    private Usuario veterinario;
    private Instant fecha;
    private Instant horaInicio;
    private Instant horaFin;
    private String motivo;

    public BloqueoVeterinario() {}

    public BloqueoVeterinario(Long id, Usuario veterinario, Instant fecha, Instant horaInicio, Instant horaFin, String motivo) {
        this.id = id;
        this.veterinario = veterinario;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
    public Instant getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Instant horaInicio) { this.horaInicio = horaInicio; }
    public Instant getHoraFin() { return horaFin; }
    public void setHoraFin(Instant horaFin) { this.horaFin = horaFin; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
