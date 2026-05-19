package com.petcare.backend.domain.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {
    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private LocalDate fechaNacimiento;
    private String microchip;
    private String condicionReproductiva;
    private String alergias;
    private String enfermedadesCronicas;
    private String alertasMedicas;
    private Boolean activo;
}
