package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Auditoria;
import java.time.Instant;
import java.util.List;

public interface AuditoriaRepositoryPort {
    Auditoria save(Auditoria auditoria);
    List<Auditoria> buscar(String tablaAfectada, Long usuarioId, Instant fechaDesde, Instant fechaHasta);
}
