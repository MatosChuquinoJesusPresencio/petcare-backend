package com.petcare.backend.web.dto;

import java.util.List;

public record DisponibilidadResponse(
        Long veterinarioId,
        String fecha,
        Integer duracionMinutos,
        List<String> horariosDisponibles
) {
}
