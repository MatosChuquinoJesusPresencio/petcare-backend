package com.petcare.backend.domain.model;

public class RecetaDetalle {
    private Long id;
    private String medicamento;
    private String presentacion;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String viaAdministracion;
    private String indicaciones;

    public RecetaDetalle() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicamento() { return medicamento; }
    public String getPresentacion() { return presentacion; }
    public String getDosis() { return dosis; }
    public String getFrecuencia() { return frecuencia; }
    public String getDuracion() { return duracion; }
    public String getViaAdministracion() { return viaAdministracion; }
    public String getIndicaciones() { return indicaciones; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public void setViaAdministracion(String viaAdministracion) { this.viaAdministracion = viaAdministracion; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }
}
