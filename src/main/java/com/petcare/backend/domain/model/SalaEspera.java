package com.petcare.backend.domain.model;

import java.time.Instant;

public class SalaEspera {
    private Long id;
    private Cita cita;
    private Mascota mascota;
    private Instant fechaLlegada;
    private String estado;
    private String observaciones;

    public SalaEspera() {}

    public SalaEspera(Long id, Cita cita, Mascota mascota, Instant fechaLlegada, String estado, String observaciones) {
        this.id = id;
        this.cita = cita;
        this.mascota = mascota;
        this.fechaLlegada = fechaLlegada;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() { return id; }
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public Instant getFechaLlegada() { return fechaLlegada; }
    public void setFechaLlegada(Instant fechaLlegada) { this.fechaLlegada = fechaLlegada; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
