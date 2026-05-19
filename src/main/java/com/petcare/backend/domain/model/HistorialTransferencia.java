package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialTransferencia {
    private Long id;
    private Mascota mascota;
    private Dueno duenoAnterior;
    private Dueno duenoNuevo;
    private LocalDateTime fecha;
    private String motivo;
    private Usuario usuarioResponsable;
}
