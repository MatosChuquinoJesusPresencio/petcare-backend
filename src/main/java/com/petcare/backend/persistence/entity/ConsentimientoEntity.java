package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false, length = 150)
    private String procedimiento;

    @Column(name = "descripcion_riesgos", columnDefinition = "TEXT")
    private String descripcionRiesgos;

    @Column(nullable = false)
    @Builder.Default
    private Boolean firmado = false;

    @Column(name = "fecha_firma")
    private LocalDateTime fechaFirma;

    @Column(name = "archivo_url", length = 255)
    private String archivoUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creado_por", nullable = false)
    private UsuarioEntity creadoPor;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
    }
}
