package com.petcare.backend.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Receta {
    private Long id;
    private Long atencionClinicaId;
    private Mascota mascota;
    private Usuario veterinario;
    private String diagnostico;
    private String notasAdicionales;
    private String estado;
    private Usuario creadoPor;
    private Instant creadoEn;
    private Usuario actualizadoPor;
    private Instant actualizadoEn;
    private List<RecetaDetalle> detalles;

    public Receta() { this.detalles = new ArrayList<>(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAtencionClinicaId() { return atencionClinicaId; }
    public Mascota getMascota() { return mascota; }
    public Usuario getVeterinario() { return veterinario; }
    public String getDiagnostico() { return diagnostico; }
    public String getNotasAdicionales() { return notasAdicionales; }
    public String getEstado() { return estado; }
    public Usuario getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public List<RecetaDetalle> getDetalles() { return detalles; }
    public void setAtencionClinicaId(Long atencionClinicaId) { this.atencionClinicaId = atencionClinicaId; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setNotasAdicionales(String notasAdicionales) { this.notasAdicionales = notasAdicionales; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
    public void setDetalles(List<RecetaDetalle> detalles) { this.detalles = detalles; }
    public Usuario getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(Usuario actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
