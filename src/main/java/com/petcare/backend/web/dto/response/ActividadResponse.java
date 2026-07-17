package com.petcare.backend.web.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ActividadResponse(
        Long id,
        String tipo,
        String descripcion,
        LocalDate fechaProgramada,
        LocalTime horaProgramada,
        String frecuencia,
        String responsable,
        String estado,
        String observaciones
) {}
