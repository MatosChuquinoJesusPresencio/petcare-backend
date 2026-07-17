package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "seguimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeguimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_clinica_id", nullable = false)
    private AtencionClinicaEntity atencionClinica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntity veterinario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dueno_notificado_id")
    private DuenoEntity duenoNotificado;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "fecha_programada", nullable = false)
    private Instant fechaProgramada;

    @Column(name = "fecha_completada")
    private Instant fechaCompletada;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String resultado;

    @Column(nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creado_por", nullable = false)
    private UsuarioEntity creadoPor;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por")
    private UsuarioEntity actualizadoPor;

    @Column(name = "actualizado_en")
    private Instant actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = Instant.now();
        this.actualizadoEn = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = Instant.now();
    }
}
