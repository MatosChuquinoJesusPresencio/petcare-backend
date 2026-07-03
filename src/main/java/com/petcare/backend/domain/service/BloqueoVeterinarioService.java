package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.port.BloqueoVeterinarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BloqueoVeterinarioService {

    private final BloqueoVeterinarioRepositoryPort bloqueoRepositoryPort;

    public BloqueoVeterinarioService(BloqueoVeterinarioRepositoryPort bloqueoRepositoryPort) {
        this.bloqueoRepositoryPort = bloqueoRepositoryPort;
    }

    public List<BloqueoVeterinario> listarPorVeterinario(Long veterinarioId) {
        return bloqueoRepositoryPort.findByVeterinarioId(veterinarioId);
    }

    public List<BloqueoVeterinario> listarPorVeterinarioYFecha(Long veterinarioId, LocalDate fecha) {
        return bloqueoRepositoryPort.findByVeterinarioIdAndFecha(veterinarioId, fecha);
    }

    public BloqueoVeterinario obtenerPorId(Long id) {
        return bloqueoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule block not found"));
    }

    @Transactional
    public BloqueoVeterinario bloquear(BloqueoVeterinario bloqueo) {
        return bloqueoRepositoryPort.save(bloqueo);
    }

    @Transactional
    public void eliminarBloqueo(Long id) {
        bloqueoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule block not found"));
        bloqueoRepositoryPort.deleteById(id);
    }
}
