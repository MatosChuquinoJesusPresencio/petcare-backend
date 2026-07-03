package com.petcare.backend.web.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueoVeterinarioResponse(
        Long id,
        Long veterinarioId,
        LocalDate fecha,
        LocalTime horaInicio,
        LocalTime horaFin,
        String motivo
) {}
