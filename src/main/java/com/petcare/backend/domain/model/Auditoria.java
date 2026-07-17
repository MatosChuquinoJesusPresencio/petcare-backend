package com.petcare.backend.domain.model;

import lombok.Setter;
import java.time.Instant;

@Setter
public class Auditoria {
    private Long id;
    private String tablaAfectada;
    private Long registroId;
    private String campo;
    private String valorAnterior;
    private String valorNuevo;
    private String tipoOperacion;
    private Usuario usuario;
    private Instant fechaCambio;
    private String motivo;

    public Auditoria() {}

    public Auditoria(Long id, String tablaAfectada, Long registroId, String campo,
                     String valorAnterior, String valorNuevo, String tipoOperacion,
                     Usuario usuario, Instant fechaCambio, String motivo) {
        this.id = id;
        this.tablaAfectada = tablaAfectada;
        this.registroId = registroId;
        this.campo = campo;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.tipoOperacion = tipoOperacion;
        this.usuario = usuario;
        this.fechaCambio = fechaCambio;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTablaAfectada() { return tablaAfectada; }
    public Long getRegistroId() { return registroId; }
    public String getCampo() { return campo; }
    public String getValorAnterior() { return valorAnterior; }
    public String getValorNuevo() { return valorNuevo; }
    public String getTipoOperacion() { return tipoOperacion; }
    public Usuario getUsuario() { return usuario; }
    public Instant getFechaCambio() { return fechaCambio; }
    public String getMotivo() { return motivo; }
}
