package com.petcare.backend.web.dto.response;

import java.math.BigDecimal;

public record ServicioResponse(
        Long id,
        String name,
        String description,
        Integer durationMinutes,
        BigDecimal referenceCost,
        Boolean active
) {}
