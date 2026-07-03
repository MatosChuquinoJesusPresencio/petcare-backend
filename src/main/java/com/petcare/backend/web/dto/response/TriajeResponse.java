package com.petcare.backend.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record TriajeResponse(
        Long id,
        Long citaId,
        String motivoVisita,
        String nivelUrgencia,
        String signosVisibles,
        String observaciones,
        BigDecimal peso,
        BigDecimal temperatura,
        Integer frecuenciaCardiaca,
        Integer frecuenciaRespiratoria,
        Long asistenteId,
        Instant creadoEn,
        Instant actualizadoEn
) {}
