package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.HistorialTransferencia;
import com.petcare.backend.domain.service.HistorialTransferenciaService;
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
    public ResponseEntity<Page<HistorialTransferencia>> listarPorMascota(@PathVariable Long mascotaId, Pageable pageable) {
        return ResponseEntity.ok(historialTransferenciaService.listarPorMascota(mascotaId, pageable));
    }
}
