package com.petcare.backend.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class BloqueoVeterinario {
    private Long id;
    private Usuario veterinario;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String motivo;

    public BloqueoVeterinario() {}

    public BloqueoVeterinario(Long id, Usuario veterinario, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String motivo) {
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
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getMotivo() { return motivo; }

    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
