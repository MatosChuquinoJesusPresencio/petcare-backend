package com.petcare.backend.domain.model;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer duracionMinutos;
    private BigDecimal costoReferencial;
    private Boolean activo;
}
