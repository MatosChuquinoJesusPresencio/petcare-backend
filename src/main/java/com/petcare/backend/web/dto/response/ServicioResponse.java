package com.petcare.backend.web.dto.response;

import java.math.BigDecimal;

public record ServicioResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer duracionMinutos,
        BigDecimal costoReferencial,
        Boolean activo
) {}
