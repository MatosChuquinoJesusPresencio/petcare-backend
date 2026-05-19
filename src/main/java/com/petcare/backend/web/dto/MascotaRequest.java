package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record MascotaRequest(
        @NotBlank(message = "El nombre de la mascota es obligatorio")
        String nombre,

        @NotBlank(message = "La especie es obligatoria")
        String especie,

        @NotBlank(message = "La raza es obligatoria")
        String raza,

        @NotBlank(message = "El sexo es obligatorio")
        @Pattern(regexp = "^(?i)(MACHO|HEMBRA)$", message = "El sexo debe ser MACHO o HEMBRA")
        String sexo,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate fechaNacimiento,

        String microchip,

        String condicionReproductiva,

        String alergias,

        String enfermedadesCronicas,

        String alertasMedicas,

        // Campos obligatorios para el registro inicial vinculando al dueño principal
        @NotNull(message = "El ID del dueño es obligatorio")
        Long duenoId,

        @NotBlank(message = "La relación con el dueño es obligatoria")
        String relacionDueno
) {
}
