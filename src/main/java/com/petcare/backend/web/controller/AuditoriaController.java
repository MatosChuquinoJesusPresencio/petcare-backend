package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Auditoria;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.web.dto.response.AuditoriaResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<AuditoriaResponse>> buscar(
            @RequestParam(value = "tabla", required = false) String tabla,
            @RequestParam(value = "usuarioId", required = false) Long usuarioId,
            @RequestParam(value = "fechaDesde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(value = "fechaHasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta) {

        ZoneId zone = ZoneId.systemDefault();
        var desde = fechaDesde != null ? fechaDesde.atZone(zone).toInstant() : null;
        var hasta = fechaHasta != null ? fechaHasta.atZone(zone).toInstant() : null;

        List<Auditoria> registros = auditoriaService.buscar(tabla, usuarioId, desde, hasta);
        List<AuditoriaResponse> response = registros.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    private AuditoriaResponse toResponse(Auditoria a) {
        return new AuditoriaResponse(
                a.getId(),
                a.getTablaAfectada(),
                a.getRegistroId(),
                a.getCampo(),
                a.getValorAnterior(),
                a.getValorNuevo(),
                a.getTipoOperacion(),
                a.getUsuario() != null ? a.getUsuario().getId() : null,
                a.getUsuario() != null ? a.getUsuario().getNombres() + " " + a.getUsuario().getApellidos() : null,
                a.getFechaCambio(),
                a.getMotivo()
        );
    }
}
