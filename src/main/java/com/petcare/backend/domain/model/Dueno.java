package com.petcare.backend.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dueno {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private String direccion;
    private Usuario usuario;
    private Boolean activo;
}
