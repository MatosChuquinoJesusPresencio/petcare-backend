package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record MascotaRequest(
        @NotBlank(message = "Pet name is required")
        String name,

        @NotBlank(message = "Species is required")
        String species,

        @NotBlank(message = "Breed is required")
        String breed,

        @NotBlank(message = "Gender is required")
        @Pattern(regexp = "^(?i)(MACHO|HEMBRA)$", message = "Gender must be MACHO or HEMBRA")
        String gender,

        @NotNull(message = "Birth date is required")
        LocalDate birthDate,

        String microchip,

        String reproductiveCondition,

        String allergies,

        String chronicDiseases,

        String medicalAlerts,

        // Required fields for initial registration linking to the main owner
        @NotNull(message = "Owner ID is required")
        Long ownerId,

        @NotBlank(message = "Relation with owner is required")
        String ownerRelation
) {
}
