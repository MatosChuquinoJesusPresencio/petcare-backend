package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.domain.port.SalaEsperaRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.model.Mascota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SalaEsperaService {

    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Lima");
    private static final List<String> ESTADOS_VALIDOS = List.of("PENDIENTE", "EN_ATENCION", "ATENDIDO", "REPROGRAMADO");
    private static final List<String> TRANSICIONES_PERMITIDAS = List.of("PENDIENTE", "REPROGRAMADO");

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
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (salaEsperaRepositoryPort.findByCitaId(citaId).isPresent()) {
            throw new BusinessRuleException("Esta cita ya está registrada en sala de espera");
        }

        Mascota mascota = cita.getMascota();
        if (mascota == null) {
            throw new BusinessRuleException("La cita no tiene una mascota asociada");
        }

        Instant fechaCita = cita.getFechaHora();
        if (fechaCita == null) {
            throw new BusinessRuleException("La cita no tiene fecha/hora definida");
        }
        LocalDate hoy = LocalDate.now(BUSINESS_ZONE);
        LocalDate fechaCitaLocal = fechaCita.atZone(BUSINESS_ZONE).toLocalDate();
        if (!fechaCitaLocal.equals(hoy)) {
            throw new BusinessRuleException("Solo se pueden registrar citas del día de hoy en sala de espera");
        }

        SalaEspera salaEspera = new SalaEspera(
                null, cita, mascota, Instant.now(), "PENDIENTE", observaciones
        );

        return salaEsperaRepositoryPort.save(salaEspera);
    }

    @Transactional
    public SalaEspera cambiarEstado(Long id, String nuevoEstado) {
        SalaEspera salaEspera = salaEsperaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de sala de espera no encontrado"));

        if (nuevoEstado == null) {
            throw new BusinessRuleException("El nuevo estado no debe ser nulo");
        }
        String estadoUpper = nuevoEstado.toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoUpper)) {
            throw new BusinessRuleException("Estado de sala de espera inválido");
        }
        if (!TRANSICIONES_PERMITIDAS.contains(estadoUpper)) {
            throw new BusinessRuleException("Solo se puede cambiar a PENDIENTE o REPROGRAMADO manualmente");
        }

        salaEspera.setEstado(estadoUpper);
        return salaEsperaRepositoryPort.save(salaEspera);
    }

    public Page<SalaEspera> listarTodas(Pageable pageable) {
        return salaEsperaRepositoryPort.findAllByOrderByFechaLlegadaAsc(pageable);
    }

    public SalaEspera obtenerPorId(Long id) {
        return salaEsperaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de sala de espera no encontrado"));
    }

    public Page<SalaEspera> listarPorEstado(String estado, Pageable pageable) {
        if (estado == null) {
            throw new BusinessRuleException("El estado no debe ser nulo");
        }
        return salaEsperaRepositoryPort.findByEstadoOrderByFechaLlegadaAsc(estado.toUpperCase(), pageable);
    }
}
