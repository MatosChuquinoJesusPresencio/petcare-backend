package com.petcare.backend.web.dto.response;

import java.time.LocalDate;

public record MascotaResponse(
        Long id,
        String nombre,
        String especie,
        String raza,
        String genero,
        LocalDate fechaNacimiento,
        String microchip,
        String condicionReproductiva,
        String alergias,
        String enfermedadesCronicas,
        String alertasMedicas,
        String notasMedicas,
        Boolean estado
) {}
