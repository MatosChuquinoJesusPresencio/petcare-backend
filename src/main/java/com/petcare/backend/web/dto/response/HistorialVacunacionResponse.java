package com.petcare.backend.web.dto.response;

import java.time.LocalDate;

public record HistorialVacunacionResponse(
        Long id,
        Long mascotaId,
        String mascotaNombre,
        String tipo,
        String nombreProducto,
        LocalDate fechaAplicacion,
        LocalDate proximaDosis,
        String lote,
        String fabricante,
        String dosis,
        String viaAdministracion,
        Long veterinarioId,
        String veterinarioNombre,
        String observaciones,
        String estado
) {}
