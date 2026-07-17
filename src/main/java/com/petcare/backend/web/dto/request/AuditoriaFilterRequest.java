package com.petcare.backend.web.dto.request;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public record AuditoriaFilterRequest(
        String tablaAfectada,
        Long usuarioId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
) {}
