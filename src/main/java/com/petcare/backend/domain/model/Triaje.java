package com.petcare.backend.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Triaje {
    private Long id;
    private Cita cita;
    private String motivoVisita;
    private String nivelUrgencia;
    private String signosVisibles;
    private String observaciones;
    private BigDecimal peso;
    private BigDecimal temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private Usuario asistente;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
