package com.petcare.backend.web.dto.response;

import java.time.LocalTime;

public record DisponibilidadVeterinarioResponse(
        Long id,
        Long veterinarioId,
        Integer diaSemana,
        LocalTime horaInicio,
        LocalTime horaFin,
        Boolean activo
) {}
