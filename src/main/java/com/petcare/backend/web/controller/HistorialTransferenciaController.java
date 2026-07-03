package com.petcare.backend.web.controller;

import com.petcare.backend.domain.service.HistorialTransferenciaService;
import com.petcare.backend.web.dto.response.HistorialTransferenciaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mascotas/{mascotaId}/transferencias")
public class HistorialTransferenciaController {

    private final HistorialTransferenciaService historialTransferenciaService;

    public HistorialTransferenciaController(HistorialTransferenciaService historialTransferenciaService) {
        this.historialTransferenciaService = historialTransferenciaService;
    }

    @GetMapping
    public ResponseEntity<Page<HistorialTransferenciaResponse>> listarPorMascota(@PathVariable Long mascotaId, Pageable pageable) {
        Page<HistorialTransferenciaResponse> page = historialTransferenciaService.listarPorMascota(mascotaId, pageable)
                .map(h -> new HistorialTransferenciaResponse(h.getId(), h.getMascota().getId(),
                        h.getDuenoAnterior().getId(), h.getDuenoNuevo().getId(), h.getFecha(),
                        h.getMotivo(), h.getUsuarioResponsable().getId()));
        return ResponseEntity.ok(page);
    }
}
