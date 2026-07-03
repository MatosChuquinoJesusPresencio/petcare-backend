package com.petcare.backend.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CambioDuenoPrincipalRequest(
    @NotNull(message = "Owner ID is required")
    Long ownerId,

    @NotBlank(message = "Relation is required")
    String relation,

    @NotBlank(message = "Reason is required")
    String reason
) {}
