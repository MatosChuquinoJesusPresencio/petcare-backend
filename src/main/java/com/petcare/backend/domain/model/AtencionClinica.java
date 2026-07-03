package com.petcare.backend.domain.model;

import java.time.Instant;

public class AtencionClinica {
    private Long id;
    private Cita cita;
    private Mascota mascota;
    private Usuario veterinario;
    private Triaje triaje;
    private String motivoConsulta;
    private String sintomas;
    private String diagnostico;
    private String tratamiento;
    private String observacionesClinicas;
    private Usuario creadoPor;
    private Instant creadoEn;
    private Usuario actualizadoPor;
    private Instant actualizadoEn;

    public AtencionClinica() {}

    public AtencionClinica(
        Long id,
        Cita cita,
        Mascota mascota,
        Usuario veterinario,
        Triaje triaje,
        String motivoConsulta,
        String sintomas,
        String diagnostico,
        String tratamiento,
        String observacionesClinicas,
        Usuario creadoPor,
        Instant creadoEn,
        Usuario actualizadoPor,
        Instant actualizadoEn
    ) {
        this.id = id;
        this.cita = cita;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.triaje = triaje;
        this.motivoConsulta = motivoConsulta;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observacionesClinicas = observacionesClinicas;
        this.creadoPor = creadoPor;
        this.creadoEn = creadoEn;
        this.actualizadoPor = actualizadoPor;
        this.actualizadoEn = actualizadoEn;
    }

    public Long getId() { return id; }
    public Cita getCita() { return cita; }
    public Mascota getMascota() { return mascota; }
    public Usuario getVeterinario() { return veterinario; }
    public Triaje getTriaje() { return triaje; }
    public String getMotivoConsulta() { return motivoConsulta; }
    public String getSintomas() { return sintomas; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public String getObservacionesClinicas() { return observacionesClinicas; }
    public Usuario getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public Usuario getActualizadoPor() { return actualizadoPor; }
    public Instant getActualizadoEn() { return actualizadoEn; }

    public void setCita(Cita cita) { this.cita = cita; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setTriaje(Triaje triaje) { this.triaje = triaje; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public void setObservacionesClinicas(String observacionesClinicas) { this.observacionesClinicas = observacionesClinicas; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public void setActualizadoPor(Usuario actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
