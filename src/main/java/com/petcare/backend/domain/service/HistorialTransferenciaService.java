package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.HistorialTransferencia;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.domain.port.HistorialTransferenciaRepositoryPort;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
public class HistorialTransferenciaService {

    private final HistorialTransferenciaRepositoryPort historialTransferenciaRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final DuenoRepositoryPort duenoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public HistorialTransferenciaService(HistorialTransferenciaRepositoryPort historialTransferenciaRepositoryPort,
                                          MascotaRepositoryPort mascotaRepositoryPort,
                                          DuenoRepositoryPort duenoRepositoryPort,
                                          UsuarioRepositoryPort usuarioRepositoryPort) {
        this.historialTransferenciaRepositoryPort = historialTransferenciaRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.duenoRepositoryPort = duenoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public HistorialTransferencia registrarTransferencia(Long mascotaId, Long duenoAnteriorId,
                                                          Long duenoNuevoId, String motivo,
                                                          Long usuarioResponsableId) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        Dueno duenoAnterior = duenoAnteriorId != null
                ? duenoRepositoryPort.findById(duenoAnteriorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Dueño anterior no encontrado"))
                : null;

        Dueno duenoNuevo = duenoRepositoryPort.findById(duenoNuevoId)
                .orElseThrow(() -> new ResourceNotFoundException("Nuevo dueño no encontrado"));

        Usuario responsable = usuarioRepositoryPort.findById(usuarioResponsableId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        HistorialTransferencia transferencia = new HistorialTransferencia(
                null, mascota, duenoAnterior, duenoNuevo, Instant.now(), motivo, responsable
        );

        return historialTransferenciaRepositoryPort.save(transferencia);
    }

    public Page<HistorialTransferencia> listarPorMascota(Long mascotaId, Pageable pageable) {
        return historialTransferenciaRepositoryPort.findByMascotaIdOrderByFechaDesc(mascotaId, pageable);
    }
}
