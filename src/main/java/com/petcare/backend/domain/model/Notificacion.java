package com.petcare.backend.domain.model;

import java.time.Instant;

public class Notificacion {
    private Long id;
    private String tipo;
    private Long destinoUsuarioId;
    private Long mascotaId;
    private Long citaId;
    private String canal;
    private String mensaje;
    private String estado;
    private Instant fechaEnvio;
    private String errorMensaje;
    private Boolean leido;
    private Instant creadoEn;

    public Notificacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getDestinoUsuarioId() { return destinoUsuarioId; }
    public void setDestinoUsuarioId(Long destinoUsuarioId) { this.destinoUsuarioId = destinoUsuarioId; }
    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Instant fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public String getErrorMensaje() { return errorMensaje; }
    public void setErrorMensaje(String errorMensaje) { this.errorMensaje = errorMensaje; }
    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
}
