package com.petcare.backend.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Triaje {
    private Long id;
    private Cita cita;
    private String motivoVisita;
    private String nivelUrgencia;
    private String signosVisibles;
    private String observaciones;
    private BigDecimal peso;
    private BigDecimal temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private Usuario asistente;
    private Instant creadoEn;
    private Instant actualizadoEn;

    public Triaje() {}

    public Triaje(Long id, Cita cita, String motivoVisita, String nivelUrgencia, String signosVisibles, String observaciones, BigDecimal peso, BigDecimal temperatura, Integer frecuenciaCardiaca, Integer frecuenciaRespiratoria, Usuario asistente, Instant creadoEn, Instant actualizadoEn) {
        this.id = id;
        this.cita = cita;
        this.motivoVisita = motivoVisita;
        this.nivelUrgencia = nivelUrgencia;
        this.signosVisibles = signosVisibles;
        this.observaciones = observaciones;
        this.peso = peso;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
        this.asistente = asistente;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    public Long getId() { return id; }
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public String getMotivoVisita() { return motivoVisita; }
    public void setMotivoVisita(String motivoVisita) { this.motivoVisita = motivoVisita; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(String nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
    public String getSignosVisibles() { return signosVisibles; }
    public void setSignosVisibles(String signosVisibles) { this.signosVisibles = signosVisibles; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
    public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }
    public Usuario getAsistente() { return asistente; }
    public void setAsistente(Usuario asistente) { this.asistente = asistente; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
