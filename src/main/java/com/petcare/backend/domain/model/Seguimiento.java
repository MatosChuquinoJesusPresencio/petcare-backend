package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguimiento {
    private Long id;
    private AtencionClinica atencion;
    private String tipo;
    private LocalDate fechaProgramada;
    private LocalDateTime fechaRealizada;
    private String estado;
    private String observaciones;
    private Usuario responsable;
    private LocalDateTime creadoEn;
}
