package com.petcare.backend.domain.model;

import java.time.LocalTime;

public class DisponibilidadVeterinario {
    private Long id;
    private Usuario veterinario;
    private Integer diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean activo;

    public DisponibilidadVeterinario() {}

    public DisponibilidadVeterinario(Long id, Usuario veterinario, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Boolean activo) {
        this.id = id;
        this.veterinario = veterinario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public Usuario getVeterinario() { return veterinario; }
    public Integer getDiaSemana() { return diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public Boolean getActivo() { return activo; }

    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
