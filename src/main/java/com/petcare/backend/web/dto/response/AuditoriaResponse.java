package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record AuditoriaResponse(
        Long id,
        String tablaAfectada,
        Long registroId,
        String campo,
        String valorAnterior,
        String valorNuevo,
        String tipoOperacion,
        Long usuarioId,
        String usuarioNombre,
        Instant fechaCambio,
        String motivo
) {}
