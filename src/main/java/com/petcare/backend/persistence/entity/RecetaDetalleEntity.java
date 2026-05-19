package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receta_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receta_id", nullable = false)
    private RecetaVeterinariaEntity receta;

    @Column(nullable = false, length = 150)
    private String medicamento;

    @Column(nullable = false, length = 100)
    private String dosis;

    @Column(nullable = false, length = 100)
    private String frecuencia;

    @Column(nullable = false, length = 100)
    private String duracion;

    @Column(columnDefinition = "TEXT")
    private String indicaciones;
}
