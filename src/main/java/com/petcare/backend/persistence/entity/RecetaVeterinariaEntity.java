package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recetas_veterinarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaVeterinariaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_id", nullable = false, unique = true)
    private AtencionClinicaEntity atencion;

    @Column(name = "indicaciones_generales", columnDefinition = "TEXT")
    private String indicacionesGenerales;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDateTime.now();
    }
}
