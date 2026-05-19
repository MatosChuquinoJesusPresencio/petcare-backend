package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {
    private Long id;
    private Mascota mascota;
    private Usuario veterinario;
    private Servicio servicio;
    private LocalDateTime fechaHora;
    private String estado;
    private String notas;
    private Usuario creadoPor;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
}
