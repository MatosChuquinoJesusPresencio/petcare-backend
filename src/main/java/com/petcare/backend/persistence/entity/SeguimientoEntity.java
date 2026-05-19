package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "atencion_id", nullable = false)
    private AtencionClinicaEntity atencion;

    @Column(nullable = false, length = 50)
    private String tipo; // LLAMADA, CONTROL_PRESENCIAL, REVISION_TRATAMIENTO

    @Column(name = "fecha_programada", nullable = false)
    private LocalDate fechaProgramada;

    @Column(name = "fecha_realizada")
    private LocalDateTime fechaRealizada;

    @Column(nullable = false, length = 30)
    private String estado; // PROGRAMADO, COMPLETADO, CANCELADO

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsable_id", nullable = false)
    private UsuarioEntity responsable;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
    }
}
