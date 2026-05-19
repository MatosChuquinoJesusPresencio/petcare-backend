package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaEspera {
    private Long id;
    private Cita cita;
    private Mascota mascota;
    private LocalDateTime fechaLlegada;
    private String estado;
    private String observaciones;
}
