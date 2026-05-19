package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atenciones_clinicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionClinicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id")
    private CitaEntity cita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntity veterinario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triaje_id")
    private TriajeEntity triaje;

    @Column(name = "motivo_consulta", nullable = false, columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(columnDefinition = "TEXT")
    private String sintomas;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "observaciones_clinicas", columnDefinition = "TEXT")
    private String observacionesClinicas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creado_por", nullable = false)
    private UsuarioEntity creadoPor;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actualizado_por")
    private UsuarioEntity actualizadoPor;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
        this.actualizadoEn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }
}
