package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Auditoria;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.AuditoriaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuditoriaService {

    private final AuditoriaRepositoryPort auditoriaRepositoryPort;

    public AuditoriaService(AuditoriaRepositoryPort auditoriaRepositoryPort) {
        this.auditoriaRepositoryPort = auditoriaRepositoryPort;
    }

    @Transactional
    public void registrarCreacion(String tablaAfectada, Long registroId,
                                  Usuario usuario, Map<String, String> campos) {
        if (campos == null || campos.isEmpty()) return;

        String resumen = campos.entrySet().stream()
                .map(e -> e.getKey() + ": " + (e.getValue() != null ? e.getValue() : "-"))
                .collect(Collectors.joining(", "));

        Auditoria auditoria = new Auditoria(
                null, tablaAfectada, registroId, "resumen",
                null, resumen, "CREATE",
                usuario, Instant.now(), null
        );
        auditoriaRepositoryPort.save(auditoria);
    }

    @Transactional
    public void registrarEdicion(String tablaAfectada, Long registroId,
                                 Usuario usuario, Map<String, String[]> cambios, String motivo) {
        if (cambios == null || cambios.isEmpty()) return;

        String resumenAnterior = cambios.entrySet().stream()
                .filter(e -> e.getValue().length > 0 && e.getValue()[0] != null)
                .map(e -> e.getKey() + ": " + e.getValue()[0])
                .collect(Collectors.joining(", "));

        String resumenNuevo = cambios.entrySet().stream()
                .filter(e -> e.getValue().length > 1 && e.getValue()[1] != null)
                .map(e -> e.getKey() + ": " + e.getValue()[1])
                .collect(Collectors.joining(", "));

        Auditoria auditoria = new Auditoria(
                null, tablaAfectada, registroId, "resumen",
                resumenAnterior.isEmpty() ? null : resumenAnterior,
                resumenNuevo.isEmpty() ? null : resumenNuevo,
                "UPDATE",
                usuario, Instant.now(), motivo
        );
        auditoriaRepositoryPort.save(auditoria);
    }

    @Transactional
    public void registrarEliminacion(String tablaAfectada, Long registroId,
                                     Usuario usuario, Map<String, String> camposEliminados) {
        if (camposEliminados == null || camposEliminados.isEmpty()) return;

        String resumen = camposEliminados.entrySet().stream()
                .map(e -> e.getKey() + ": " + (e.getValue() != null ? e.getValue() : "-"))
                .collect(Collectors.joining(", "));

        Auditoria auditoria = new Auditoria(
                null, tablaAfectada, registroId, "resumen",
                resumen, null, "DELETE",
                usuario, Instant.now(), null
        );
        auditoriaRepositoryPort.save(auditoria);
    }

    public List<Auditoria> buscar(String tablaAfectada, Long usuarioId,
                                   Instant fechaDesde, Instant fechaHasta) {
        return auditoriaRepositoryPort.buscar(tablaAfectada, usuarioId, fechaDesde, fechaHasta);
    }
}
