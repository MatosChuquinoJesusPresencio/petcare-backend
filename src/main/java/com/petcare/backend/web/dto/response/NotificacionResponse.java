package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record NotificacionResponse(
        Long id,
        String tipo,
        Long destinoUsuarioId,
        Long mascotaId,
        Long citaId,
        String canal,
        String mensaje,
        String estado,
        Instant fechaEnvio,
        String errorMensaje,
        Boolean leido,
        Instant creadoEn
) {}
