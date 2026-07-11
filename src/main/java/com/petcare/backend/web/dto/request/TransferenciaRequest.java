package com.petcare.backend.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransferenciaRequest(
        @NotNull(message = "El ID del nuevo dueño es obligatorio")
        Long nuevoDuenoId,

        @NotBlank(message = "El motivo es obligatorio")
        String motivo
) {
}
