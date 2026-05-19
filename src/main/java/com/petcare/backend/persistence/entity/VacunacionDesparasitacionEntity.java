package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacunaciones_desparasitaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacunacionDesparasitacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntity veterinario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_id")
    private AtencionClinicaEntity atencion;

    @Column(nullable = false, length = 30)
    private String tipo; // VACUNA, DESPARASITACION

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(nullable = false, length = 50)
    private String dosis;

    @Column(name = "fecha_aplicacion", nullable = false)
    private LocalDate fechaAplicacion;

    @Column(name = "proxima_dosis_fecha")
    private LocalDate proximaDosisFecha;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
    }
}
