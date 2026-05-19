package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanTratamiento {
    private Long id;
    private Mascota mascota;
    private AtencionClinica atencion;
    private String descripcion;
    private String estado;
    private Usuario creadoPor;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
