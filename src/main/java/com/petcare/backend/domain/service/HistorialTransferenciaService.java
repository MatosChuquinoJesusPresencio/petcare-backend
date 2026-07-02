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

@Service
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
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        Dueno duenoAnterior = duenoAnteriorId != null
                ? duenoRepositoryPort.findById(duenoAnteriorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Previous owner not found"))
                : null;

        Dueno duenoNuevo = duenoRepositoryPort.findById(duenoNuevoId)
                .orElseThrow(() -> new ResourceNotFoundException("New owner not found"));

        Usuario responsable = usuarioRepositoryPort.findById(usuarioResponsableId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        HistorialTransferencia transferencia = HistorialTransferencia.builder()
                .mascota(mascota)
                .duenoAnterior(duenoAnterior)
                .duenoNuevo(duenoNuevo)
                .motivo(motivo)
                .usuarioResponsable(responsable)
                .build();

        return historialTransferenciaRepositoryPort.save(transferencia);
    }

    public Page<HistorialTransferencia> listarPorMascota(Long mascotaId, Pageable pageable) {
        return historialTransferenciaRepositoryPort.findByMascotaIdOrderByFechaDesc(mascotaId, pageable);
    }
}
