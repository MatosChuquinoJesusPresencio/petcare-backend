package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "consentimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dueno_id", nullable = false)
    private DuenoEntity dueno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_clinica_id")
    private AtencionClinicaEntity atencionClinica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntity veterinario;

    @Column(name = "tipo_procedimiento", nullable = false, length = 100)
    private String tipoProcedimiento;

    @Column(name = "descripcion_procedimiento", nullable = false, columnDefinition = "TEXT")
    private String descripcionProcedimiento;

    @Column(name = "riesgos_descritos", nullable = false, columnDefinition = "TEXT")
    private String riesgosDescritos;

    @Column(columnDefinition = "TEXT")
    private String alternativas;

    @Column(nullable = false)
    private Boolean consentido;

    @Column(name = "fecha_consentimiento", nullable = false)
    private Instant fechaConsentimiento;

    @Column(name = "dueno_nombre_verificado", length = 100)
    private String duenoNombreVerificado;

    @Column(name = "testigo_nombre", length = 100)
    private String testigoNombre;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creado_por", nullable = false)
    private UsuarioEntity creadoPor;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = Instant.now();
        this.fechaConsentimiento = Instant.now();
    }
}
