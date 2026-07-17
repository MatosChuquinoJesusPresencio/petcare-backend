package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(name = "destino_usuario_id", nullable = false)
    private Long destinoUsuarioId;

    @Column(name = "mascota_id")
    private Long mascotaId;

    @Column(name = "cita_id")
    private Long citaId;

    @Column(nullable = false, length = 20)
    private String canal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_envio")
    private Instant fechaEnvio;

    @Column(name = "error_mensaje", columnDefinition = "TEXT")
    private String errorMensaje;

    @Column(nullable = false)
    private Boolean leido = false;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = Instant.now();
    }
}
