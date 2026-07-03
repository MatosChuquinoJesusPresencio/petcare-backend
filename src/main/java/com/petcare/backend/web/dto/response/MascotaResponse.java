package com.petcare.backend.web.dto.response;

import java.time.LocalDate;

public record MascotaResponse(
        Long id,
        String name,
        String especie,
        String breed,
        String gender,
        LocalDate dateOfBirth,
        String microchip,
        String reproductiveCondition,
        String allergies,
        String chronicDiseases,
        String medicalAlerts,
        String medicalNotes,
        Boolean active
) {}
