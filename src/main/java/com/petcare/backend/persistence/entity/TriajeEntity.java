package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "triajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriajeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private CitaEntity cita;

    @Column(name = "motivo_visita", nullable = false, columnDefinition = "TEXT")
    private String motivoVisita;

    @Column(name = "nivel_urgencia", nullable = false, length = 30)
    private String nivelUrgencia;

    @Column(name = "signos_visibles", columnDefinition = "TEXT")
    private String signosVisibles;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 4, scale = 1)
    private BigDecimal temperatura;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asistente_id", nullable = false)
    private UsuarioEntity asistente;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

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
