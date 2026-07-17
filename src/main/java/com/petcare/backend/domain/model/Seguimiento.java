package com.petcare.backend.domain.model;

import java.time.Instant;

public class Seguimiento {
    private Long id;
    private Long atencionClinicaId;
    private Mascota mascota;
    private Usuario veterinario;
    private Long duenoNotificadoId;
    private String tipo;
    private Instant fechaProgramada;
    private Instant fechaCompletada;
    private String motivo;
    private String resultado;
    private String estado;
    private Usuario creadoPor;
    private Instant creadoEn;

    public Seguimiento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAtencionClinicaId() { return atencionClinicaId; }
    public Mascota getMascota() { return mascota; }
    public Usuario getVeterinario() { return veterinario; }
    public Long getDuenoNotificadoId() { return duenoNotificadoId; }
    public String getTipo() { return tipo; }
    public Instant getFechaProgramada() { return fechaProgramada; }
    public Instant getFechaCompletada() { return fechaCompletada; }
    public String getMotivo() { return motivo; }
    public String getResultado() { return resultado; }
    public String getEstado() { return estado; }
    public Usuario getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setAtencionClinicaId(Long atencionClinicaId) { this.atencionClinicaId = atencionClinicaId; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setDuenoNotificadoId(Long duenoNotificadoId) { this.duenoNotificadoId = duenoNotificadoId; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setFechaProgramada(Instant fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    public void setFechaCompletada(Instant fechaCompletada) { this.fechaCompletada = fechaCompletada; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
}
