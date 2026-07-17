package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "historial_vacunaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialVacunacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "fecha_aplicacion", nullable = false)
    private LocalDate fechaAplicacion;

    @Column(name = "proxima_dosis")
    private LocalDate proximaDosis;

    @Column(length = 50)
    private String lote;

    @Column(length = 100)
    private String fabricante;

    @Column(length = 50)
    private String dosis;

    @Column(name = "via_administracion", length = 50)
    private String viaAdministracion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private UsuarioEntity veterinario;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

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
