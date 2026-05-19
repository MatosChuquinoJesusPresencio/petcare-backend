package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacunacionDesparasitacion {
    private Long id;
    private Mascota mascota;
    private Usuario veterinario;
    private AtencionClinica atencion;
    private String tipo;
    private String nombreProducto;
    private String dosis;
    private LocalDate fechaAplicacion;
    private LocalDate proximaDosisFecha;
    private String observaciones;
    private LocalDateTime creadoEn;
}
