package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name = "costo_referencial", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoReferencial;

    @Column(nullable = false)
    private Boolean activo;

    @PrePersist
    protected void onCreate() {
        this.activo = true;
    }
}
