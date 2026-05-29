package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CambioDuenoPrincipalRequest(
    @NotNull(message = "Owner ID is required")
    Long duenoId,

    @NotBlank(message = "Relation is required")
    String relacion
) {}
