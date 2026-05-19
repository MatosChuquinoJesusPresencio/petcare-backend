package com.petcare.backend.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaDetalle {
    private Long id;
    private RecetaVeterinaria receta;
    private String medicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;
}
