package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsentimientoRequest(
        @NotNull Long mascotaId,
        @NotNull Long duenoId,
        Long atencionClinicaId,
        @NotNull Long veterinarioId,
        @NotBlank String tipoProcedimiento,
        @NotBlank String descripcionProcedimiento,
        @NotBlank String riesgosDescritos,
        String alternativas,
        @NotNull Boolean consentido,
        String duenoNombreVerificado,
        String testigoNombre,
        String observaciones
) {}
