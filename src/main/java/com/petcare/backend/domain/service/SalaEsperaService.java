package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.domain.port.SalaEsperaRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalaEsperaService {

    private final SalaEsperaRepositoryPort salaEsperaRepositoryPort;
    private final CitaRepositoryPort citaRepositoryPort;

    public SalaEsperaService(SalaEsperaRepositoryPort salaEsperaRepositoryPort,
                              CitaRepositoryPort citaRepositoryPort) {
        this.salaEsperaRepositoryPort = salaEsperaRepositoryPort;
        this.citaRepositoryPort = citaRepositoryPort;
    }

    @Transactional
    public SalaEspera registrarLlegada(Long citaId, String observaciones) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (salaEsperaRepositoryPort.findByCitaId(citaId).isPresent()) {
            throw new BusinessRuleException("This appointment is already registered in the waiting room");
        }

        SalaEspera salaEspera = SalaEspera.builder()
                .cita(cita)
                .mascota(cita.getMascota())
                .fechaLlegada(LocalDateTime.now())
                .estado("PENDIENTE")
                .observaciones(observaciones)
                .build();

        return salaEsperaRepositoryPort.save(salaEspera);
    }

    @Transactional
    public SalaEspera cambiarEstado(Long id, String nuevoEstado) {
        SalaEspera salaEspera = salaEsperaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Waiting room entry not found"));

        String estadoUpper = nuevoEstado.toUpperCase();
        if (!List.of("PENDIENTE", "EN_ATENCION", "ATENDIDO", "NO_ASISTIO", "REPROGRAMADO").contains(estadoUpper)) {
            throw new BusinessRuleException("Invalid waiting room status");
        }

        salaEspera.setEstado(estadoUpper);
        return salaEsperaRepositoryPort.save(salaEspera);
    }

    public List<SalaEspera> listarTodas() {
        return salaEsperaRepositoryPort.findAllByOrderByFechaLlegadaAsc();
    }

    public List<SalaEspera> listarPorEstado(String estado) {
        return salaEsperaRepositoryPort.findByEstadoOrderByFechaLlegadaAsc(estado.toUpperCase());
    }
}
