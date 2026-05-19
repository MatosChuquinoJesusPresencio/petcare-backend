package com.petcare.backend.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaResponsable {
    private Long id;
    private Mascota mascota;
    private Dueno dueno;
    private Boolean esPrincipal;
    private String relacion;
}
