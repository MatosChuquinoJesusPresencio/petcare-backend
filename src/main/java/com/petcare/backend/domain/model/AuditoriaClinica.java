package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaClinica {
    private Long id;
    private Usuario usuario;
    private LocalDateTime fecha;
    private String moduloAfectado;
    private Long registroId;
    private String valorAnterior;
    private String valorNuevo;
    private String motivoCambio;
}
