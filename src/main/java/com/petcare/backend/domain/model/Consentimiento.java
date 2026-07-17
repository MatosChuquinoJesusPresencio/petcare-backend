package com.petcare.backend.domain.model;

import java.time.Instant;

public class Consentimiento {
    private Long id;
    private Mascota mascota;
    private Long duenoId;
    private Long atencionClinicaId;
    private Usuario veterinario;
    private String tipoProcedimiento;
    private String descripcionProcedimiento;
    private String riesgosDescritos;
    private String alternativas;
    private Boolean consentido;
    private Instant fechaConsentimiento;
    private String duenoNombreVerificado;
    private String testigoNombre;
    private String observaciones;
    private Usuario creadoPor;
    private Instant creadoEn;

    public Consentimiento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Mascota getMascota() { return mascota; }
    public Long getDuenoId() { return duenoId; }
    public Long getAtencionClinicaId() { return atencionClinicaId; }
    public Usuario getVeterinario() { return veterinario; }
    public String getTipoProcedimiento() { return tipoProcedimiento; }
    public String getDescripcionProcedimiento() { return descripcionProcedimiento; }
    public String getRiesgosDescritos() { return riesgosDescritos; }
    public String getAlternativas() { return alternativas; }
    public Boolean getConsentido() { return consentido; }
    public Instant getFechaConsentimiento() { return fechaConsentimiento; }
    public void setFechaConsentimiento(Instant fechaConsentimiento) { this.fechaConsentimiento = fechaConsentimiento; }
    public String getDuenoNombreVerificado() { return duenoNombreVerificado; }
    public String getTestigoNombre() { return testigoNombre; }
    public String getObservaciones() { return observaciones; }
    public Usuario getCreadoPor() { return creadoPor; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public void setDuenoId(Long duenoId) { this.duenoId = duenoId; }
    public void setAtencionClinicaId(Long atencionClinicaId) { this.atencionClinicaId = atencionClinicaId; }
    public void setVeterinario(Usuario veterinario) { this.veterinario = veterinario; }
    public void setTipoProcedimiento(String tipoProcedimiento) { this.tipoProcedimiento = tipoProcedimiento; }
    public void setDescripcionProcedimiento(String descripcionProcedimiento) { this.descripcionProcedimiento = descripcionProcedimiento; }
    public void setRiesgosDescritos(String riesgosDescritos) { this.riesgosDescritos = riesgosDescritos; }
    public void setAlternativas(String alternativas) { this.alternativas = alternativas; }
    public void setConsentido(Boolean consentido) { this.consentido = consentido; }
    public void setDuenoNombreVerificado(String duenoNombreVerificado) { this.duenoNombreVerificado = duenoNombreVerificado; }
    public void setTestigoNombre(String testigoNombre) { this.testigoNombre = testigoNombre; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }
}
