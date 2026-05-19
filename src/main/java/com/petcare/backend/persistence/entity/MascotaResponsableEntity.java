package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mascota_responsables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaResponsableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dueno_id", nullable = false)
    private DuenoEntity dueno;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @Column(length = 50)
    private String relacion;

    @PrePersist
    protected void onCreate() {
        this.esPrincipal = false;
    }
}
