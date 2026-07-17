package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "plan_tratamiento_actividades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanTratamientoActividadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_tratamiento_id", nullable = false)
    private PlanTratamientoEntity planTratamiento;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    @Column(name = "hora_programada")
    private LocalTime horaProgramada;

    @Column(length = 100)
    private String frecuencia;

    @Column(length = 100)
    private String responsable;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
