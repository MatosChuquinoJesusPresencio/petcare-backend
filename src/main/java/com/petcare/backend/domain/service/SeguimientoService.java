package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.port.*;
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SeguimientoService {

    private static final Logger log = LoggerFactory.getLogger(SeguimientoService.class);

    private final SeguimientoRepositoryPort repositoryPort;
    private final AtencionClinicaRepositoryPort atencionClinicaRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final NotificacionService notificacionService;

    public SeguimientoService(SeguimientoRepositoryPort repositoryPort,
                               AtencionClinicaRepositoryPort atencionClinicaRepositoryPort,
                               MascotaRepositoryPort mascotaRepositoryPort,
                               UsuarioRepositoryPort usuarioRepositoryPort,
                               NotificacionService notificacionService) {
        this.repositoryPort = repositoryPort;
        this.atencionClinicaRepositoryPort = atencionClinicaRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public Seguimiento programar(Long atencionClinicaId, Seguimiento seguimiento, Long veterinarioId, Long creadoPorId) {
        var atencion = atencionClinicaRepositoryPort.findById(atencionClinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Atención clínica no encontrada"));
        Mascota mascota = mascotaRepositoryPort.findById(atencion.getMascota().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        Usuario creador = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        seguimiento.setAtencionClinicaId(atencionClinicaId);
        seguimiento.setMascota(mascota);
        seguimiento.setVeterinario(veterinario);
        seguimiento.setCreadoPor(creador);
        seguimiento.setEstado(seguimiento.getEstado() != null ? seguimiento.getEstado() : "PROGRAMADO");

        Seguimiento saved = repositoryPort.save(seguimiento);

        notificacionService.registrar(
                "SEGUIMIENTO_PROGRAMADO",
                veterinarioId,
                mascota.getId(),
                atencion.getCita() != null ? atencion.getCita().getId() : null,
                "PLATAFORMA",
                String.format("Se programó un seguimiento para la mascota %s. Fecha: %s",
                        mascota.getNombre(),
                        saved.getFechaProgramada() != null ? saved.getFechaProgramada().toString() : "pendiente")
        );

        return saved;
    }

    @Transactional
    public Seguimiento completar(Long id, String resultado) {
        Seguimiento s = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento no encontrado"));
        if ("COMPLETADO".equals(s.getEstado()) || "CANCELADO".equals(s.getEstado())) {
            throw new BusinessRuleException("No se puede modificar un seguimiento " + s.getEstado().toLowerCase());
        }
        s.setEstado("COMPLETADO");
        s.setResultado(resultado);
        s.setFechaCompletada(Instant.now());
        return repositoryPort.save(s);
    }

    @Transactional
    public Seguimiento cancelar(Long id) {
        Seguimiento s = repositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seguimiento no encontrado"));
        s.setEstado("CANCELADO");
        return repositoryPort.save(s);
    }

    public List<Seguimiento> listarPorAtencion(Long atencionClinicaId) {
        return repositoryPort.findByAtencionClinicaId(atencionClinicaId);
    }

    public List<Seguimiento> listarPorMascota(Long mascotaId) {
        return repositoryPort.findByMascotaId(mascotaId);
    }

    public List<Seguimiento> obtenerProximos() {
        Instant ahora = Instant.now();
        Instant finSemana = ahora.plus(7 * 24 * 60 * 60, java.time.temporal.ChronoUnit.SECONDS);
        return repositoryPort.findByFechaProgramadaBetween(ahora, finSemana);
    }

    public Optional<Seguimiento> obtenerPorId(Long id) {
        return repositoryPort.findById(id);
    }
}
