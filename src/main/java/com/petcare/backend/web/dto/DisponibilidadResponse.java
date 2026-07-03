package com.petcare.backend.web.dto;

import java.util.List;

public record DisponibilidadResponse(
        Long veterinarianId,
        String date,
        Integer durationMinutes,
        List<String> availableSlots
) {
}
