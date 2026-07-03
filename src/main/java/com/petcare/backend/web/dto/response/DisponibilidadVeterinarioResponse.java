package com.petcare.backend.web.dto.response;

import java.time.LocalTime;

public record DisponibilidadVeterinarioResponse(
        Long id,
        Long veterinarianId,
        Integer dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Boolean active
) {}
