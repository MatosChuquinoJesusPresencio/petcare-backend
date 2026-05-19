package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consentimiento {
    private Long id;
    private Mascota mascota;
    private Dueno dueno;
    private String procedimiento;
    private String descripcionRiesgos;
    private Boolean firmado;
    private LocalDateTime fechaFirma;
    private String archivoUrl;
    private Usuario creadoPor;
    private LocalDateTime creadoEn;
}
