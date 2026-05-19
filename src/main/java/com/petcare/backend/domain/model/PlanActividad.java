package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanActividad {
    private Long id;
    private PlanTratamiento plan;
    private String descripcion;
    private String tipo;
    private LocalDate fechaProgramada;
    private Usuario responsable;
    private String estado;
    private String observaciones;
}
