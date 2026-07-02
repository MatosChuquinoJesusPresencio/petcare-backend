package com.petcare.backend.domain.model;

import java.time.Instant;

public class DisponibilidadVeterinario {
    private Long id;
    private Usuario veterinario;
    private Integer diaSemana;
    private Instant horaInicio;
    private Instant horaFin;
    private Boolean activo;

    public DisponibilidadVeterinario() {}

    public DisponibilidadVeterinario(Long id, Usuario veterinario, Integer diaSemana, Instant horaInicio, Instant horaFin, Boolean activo) {
        this.id = id;
        this.veterinario = veterinario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getVeterinario() { return veterinario; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }
    public Instant getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Instant horaInicio) { this.horaInicio = horaInicio; }
    public Instant getHoraFin() { return horaFin; }
    public void setHoraFin(Instant horaFin) { this.horaFin = horaFin; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
