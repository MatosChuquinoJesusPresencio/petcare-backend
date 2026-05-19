package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_transferencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialTransferenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mascota_id", nullable = false)
    private MascotaEntity mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dueno_anterior_id")
    private DuenoEntity duenoAnterior;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dueno_nuevo_id", nullable = false)
    private DuenoEntity duenoNuevo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_responsable_id", nullable = false)
    private UsuarioEntity usuarioResponsable;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}
