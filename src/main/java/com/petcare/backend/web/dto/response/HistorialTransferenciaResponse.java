package com.petcare.backend.web.dto.response;

import java.time.Instant;

public record HistorialTransferenciaResponse(
        Long id,
        Long mascotaId,
        Long duenoAnteriorId,
        Long duenoNuevoId,
        Instant fecha,
        String motivo,
        Long usuarioResponsableId
) {}
