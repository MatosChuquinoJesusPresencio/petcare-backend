package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CambioDuenoPrincipalRequest(
    @NotNull(message = "El ID del dueño es obligatorio")
    Long ownerId,

    @NotBlank(message = "La relación es obligatoria")
    String relation,

    @NotBlank(message = "El motivo es obligatorio")
    String reason
) {}
