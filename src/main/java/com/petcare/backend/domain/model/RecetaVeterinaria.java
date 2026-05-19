package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaVeterinaria {
    private Long id;
    private AtencionClinica atencion;
    private String indicacionesGenerales;
    private LocalDateTime creadoEn;
}
