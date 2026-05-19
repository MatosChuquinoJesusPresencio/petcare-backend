package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_clinica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaClinicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "modulo_afectado", nullable = false, length = 100)
    private String moduloAfectado;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Column(name = "valor_anterior", columnDefinition = "TEXT")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "TEXT")
    private String valorNuevo;

    @Column(name = "motivo_cambio", nullable = false, columnDefinition = "TEXT")
    private String motivoCambio;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}
