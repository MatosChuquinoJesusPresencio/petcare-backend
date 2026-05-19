package com.petcare.backend.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoEmergencia {
    private Long id;
    private Dueno dueno;
    private String nombre;
    private String telefono;
    private String relacion;
}
