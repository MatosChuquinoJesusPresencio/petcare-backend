package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "plan_actividades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanActividadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanTratamientoEntity plan;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo; // MEDICACION, CONTROL, ACTIVIDAD

    @Column(name = "fecha_programada", nullable = false)
    private LocalDate fechaProgramada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private UsuarioEntity responsable;

    @Column(nullable = false, length = 30)
    private String estado; // PENDIENTE, REALIZADO, CANCELADO

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
