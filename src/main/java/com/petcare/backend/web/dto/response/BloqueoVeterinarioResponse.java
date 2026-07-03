package com.petcare.backend.web.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueoVeterinarioResponse(
        Long id,
        Long veterinarianId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String reason
) {}
