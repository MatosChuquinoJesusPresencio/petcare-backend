package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record MascotaRequest(
        @NotBlank(message = "El nombre de la mascota es obligatorio")
        String name,

        @NotBlank(message = "La especie es obligatoria")
        String species,

        @NotBlank(message = "La raza es obligatoria")
        String breed,

        @NotBlank(message = "El género es obligatorio")
        @Pattern(regexp = "^(?i)(MACHO|HEMBRA)$", message = "El género debe ser MACHO o HEMBRA")
        String gender,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate birthDate,

        String microchip,

        String reproductiveCondition,

        String allergies,

        String chronicDiseases,

        String medicalAlerts,

        // Optional — only used for initial registration
        Long ownerId,

        String ownerRelation
) {
}
