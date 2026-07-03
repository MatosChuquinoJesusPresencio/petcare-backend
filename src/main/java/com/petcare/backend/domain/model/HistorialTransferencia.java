package com.petcare.backend.domain.model;

import java.time.Instant;

public class HistorialTransferencia {
    private Long id;
    private Mascota mascota;
    private Dueno duenoAnterior;
    private Dueno duenoNuevo;
    private Instant fecha;
    private String motivo;
    private Usuario usuarioResponsable;

    public HistorialTransferencia() {}

    public HistorialTransferencia(Long id, Mascota mascota, Dueno duenoAnterior, Dueno duenoNuevo, Instant fecha, String motivo, Usuario usuarioResponsable) {
        this.id = id;
        this.mascota = mascota;
        this.duenoAnterior = duenoAnterior;
        this.duenoNuevo = duenoNuevo;
        this.fecha = fecha;
        this.motivo = motivo;
        this.usuarioResponsable = usuarioResponsable;
    }

    public Long getId() { return id; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public Dueno getDuenoAnterior() { return duenoAnterior; }
    public void setDuenoAnterior(Dueno duenoAnterior) { this.duenoAnterior = duenoAnterior; }
    public Dueno getDuenoNuevo() { return duenoNuevo; }
    public void setDuenoNuevo(Dueno duenoNuevo) { this.duenoNuevo = duenoNuevo; }
    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Usuario getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(Usuario usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }
}
