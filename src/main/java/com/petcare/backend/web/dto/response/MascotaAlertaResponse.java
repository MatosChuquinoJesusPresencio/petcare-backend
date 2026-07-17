package com.petcare.backend.web.dto.response;

public record MascotaAlertaResponse(
        Long id,
        String nombre,
        String especie,
        String raza,
        String alergias,
        String enfermedadesCronicas,
        String alertasMedicas,
        String notasMedicas,
        boolean tieneAlertas
) {}
