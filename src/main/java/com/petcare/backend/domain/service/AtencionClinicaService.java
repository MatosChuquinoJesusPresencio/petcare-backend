package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.AtencionClinicaRepositoryPort;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.domain.port.TriajeRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AtencionClinicaService {

    private final AtencionClinicaRepositoryPort atencionClinicaRepositoryPort;
    private final CitaRepositoryPort citaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final TriajeRepositoryPort triajeRepositoryPort;

    public AtencionClinicaService(AtencionClinicaRepositoryPort atencionClinicaRepositoryPort,
                                   CitaRepositoryPort citaRepositoryPort,
                                   UsuarioRepositoryPort usuarioRepositoryPort,
                                   TriajeRepositoryPort triajeRepositoryPort) {
        this.atencionClinicaRepositoryPort = atencionClinicaRepositoryPort;
        this.citaRepositoryPort = citaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.triajeRepositoryPort = triajeRepositoryPort;
    }

    @Transactional
    public AtencionClinica registrar(Long citaId, String motivoConsulta, String sintomas,
                                       String diagnostico, String tratamiento, String observacionesClinicas,
                                       Long veterinarioId, Long triajeId) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));

        if (!"VETERINARIO".equalsIgnoreCase(veterinario.getRol())) {
            throw new BusinessRuleException("Only veterinarians can register clinical care");
        }

        Mascota mascota = cita.getMascota();

        Triaje triaje = null;
        if (triajeId != null) {
            triaje = triajeRepositoryPort.findById(triajeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Triage not found"));
        }

        AtencionClinica atencion = new AtencionClinica(
                null, cita, mascota, veterinario, triaje,
                motivoConsulta, sintomas, diagnostico, tratamiento, observacionesClinicas,
                veterinario, Instant.now(), null, null
        );

        return atencionClinicaRepositoryPort.save(atencion);
    }

    @Transactional
    public AtencionClinica actualizar(Long id, String motivoConsulta, String sintomas,
                                       String diagnostico, String tratamiento, String observacionesClinicas,
                                       Long usuarioId) {
        AtencionClinica existente = atencionClinicaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical care record not found"));

        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existente.setMotivoConsulta(motivoConsulta);
        existente.setSintomas(sintomas);
        existente.setDiagnostico(diagnostico);
        existente.setTratamiento(tratamiento);
        existente.setObservacionesClinicas(observacionesClinicas);
        existente.setActualizadoPor(usuario);
        existente.setActualizadoEn(Instant.now());

        return atencionClinicaRepositoryPort.save(existente);
    }

    public Page<AtencionClinica> listarPorMascota(Long mascotaId, Pageable pageable) {
        return atencionClinicaRepositoryPort.findByMascotaIdOrderByCreadoEnDesc(mascotaId, pageable);
    }

    public AtencionClinica obtenerPorId(Long id) {
        return atencionClinicaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical care record not found"));
    }

    public AtencionClinica obtenerPorCitaId(Long citaId) {
        return atencionClinicaRepositoryPort.findByCitaId(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical care record not found for this appointment"));
    }
}
