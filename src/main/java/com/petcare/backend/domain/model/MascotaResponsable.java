package com.petcare.backend.domain.model;

public class MascotaResponsable {
    private Long id;
    private Mascota mascota;
    private Dueno dueno;
    private Boolean esPrincipal;
    private String relacion;

    public MascotaResponsable() {}

    public MascotaResponsable(Long id, Mascota mascota, Dueno dueno, Boolean esPrincipal, String relacion) {
        this.id = id;
        this.mascota = mascota;
        this.dueno = dueno;
        this.esPrincipal = esPrincipal;
        this.relacion = relacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public Dueno getDueno() { return dueno; }
    public void setDueno(Dueno dueno) { this.dueno = dueno; }
    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }
    public String getRelacion() { return relacion; }
    public void setRelacion(String relacion) { this.relacion = relacion; }
}
