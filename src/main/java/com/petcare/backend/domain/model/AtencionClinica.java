package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionClinica {
    private Long id;
    private Cita cita;
    private Mascota mascota;
    private Usuario veterinario;
    private Triaje triaje;
    private String motivoConsulta;
    private String sintomas;
    private String diagnostico;
    private String observacionesClinicas;
    private Usuario creadoPor;
    private LocalDateTime creadoEn;
    private Usuario actualizadoPor;
    private LocalDateTime actualizadoEn;
}
