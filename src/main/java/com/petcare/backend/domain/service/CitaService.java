package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.BloqueoVeterinarioRepositoryPort;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.domain.port.DisponibilidadVeterinarioRepositoryPort;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ScheduleConflictException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CitaService {
    private static final Logger log = LoggerFactory.getLogger(CitaService.class);
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Lima");

    private final CitaRepositoryPort citaRepositoryPort;
    private final BloqueoVeterinarioRepositoryPort bloqueoRepositoryPort;
    private final DisponibilidadVeterinarioRepositoryPort disponibilidadRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final ServicioRepositoryPort servicioRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final NotificacionService notificacionService;

    public CitaService(CitaRepositoryPort citaRepositoryPort,
                       BloqueoVeterinarioRepositoryPort bloqueoRepositoryPort,
                       DisponibilidadVeterinarioRepositoryPort disponibilidadRepositoryPort,
                       MascotaRepositoryPort mascotaRepositoryPort,
                       ServicioRepositoryPort servicioRepositoryPort,
                       UsuarioRepositoryPort usuarioRepositoryPort,
                       NotificacionService notificacionService) {
        this.citaRepositoryPort = citaRepositoryPort;
        this.bloqueoRepositoryPort = bloqueoRepositoryPort;
        this.disponibilidadRepositoryPort = disponibilidadRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.servicioRepositoryPort = servicioRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public Cita agendarCita(Long mascotaId, Long veterinarioId, Long servicioId, LocalDateTime fechaHora, String notas, Long creadoPorId) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        
        if (!"VETERINARIO".equalsIgnoreCase(veterinario.getRol())) {
            throw new BusinessRuleException("El usuario asignado no tiene el rol VETERINARIO");
        }

        if (!Boolean.TRUE.equals(veterinario.getEstado())) {
            throw new BusinessRuleException("El veterinario seleccionado no está activo");
        }

        Servicio servicio = servicioRepositoryPort.findById(servicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        if (!servicio.getActivo()) {
            throw new BusinessRuleException("El servicio seleccionado no está activo");
        }

        Usuario creadoPor = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        Instant fechaHoraInstant = toBusinessInstant(fechaHora);
        validarDisponibilidadYHorarios(veterinarioId, servicio, fechaHoraInstant, null);

        Cita cita = new Cita(
                null, mascota, veterinario, servicio, fechaHoraInstant,
                "AGENDADA", notas, creadoPor, Instant.now(), null, Instant.now()
        );

        Cita saved = citaRepositoryPort.save(cita);

        String nombreMascota = mascota.getNombre();
        notificacionService.enviar(
                "CITA_RECORDATORIO",
                creadoPorId,
                mascotaId,
                saved.getId(),
                "SMS",
                String.format("PetCare: Su cita para %s esta programada para el %s. Servicio: %s.",
                        nombreMascota, fechaHora.toString(), servicio.getNombre())
        );

        return saved;
    }

    @Transactional
    public Cita reprogramarCita(Long citaId, LocalDateTime nuevaFechaHora) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if ("CANCELADA".equals(cita.getEstado()) || "ATENDIDA".equals(cita.getEstado())) {
            throw new BusinessRuleException("No se puede reprogramar una cita cancelada o atendida");
        }

        Instant nuevaFechaHoraInstant = toBusinessInstant(nuevaFechaHora);
        validarDisponibilidadYHorarios(cita.getVeterinario().getId(), cita.getServicio(), nuevaFechaHoraInstant, citaId);

        cita.setFechaHora(nuevaFechaHoraInstant);
        cita.setEstado("REPROGRAMADA");
        cita.setActualizadoEn(Instant.now());
        return citaRepositoryPort.save(cita);
    }

    @Transactional
    public Cita cambiarEstadoCita(Long citaId, String nuevoEstado) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (nuevoEstado == null) {
            throw new BusinessRuleException("El nuevo estado no debe ser nulo");
        }
        String estadoUpper = nuevoEstado.toUpperCase();
        if (!List.of("AGENDADA", "CONFIRMADA", "REPROGRAMADA", "CANCELADA", "ATENDIDA", "NO_ASISTIO").contains(estadoUpper)) {
            throw new BusinessRuleException("Estado de cita inválido");
        }

        cita.setEstado(estadoUpper);
        cita.setActualizadoEn(Instant.now());
        return citaRepositoryPort.save(cita);
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepositoryPort.findById(id);
    }

    public Page<Cita> listarTodas(Pageable pageable) {
        return citaRepositoryPort.findAll(pageable);
    }

    public Page<Cita> listarConFiltros(Long mascotaId, Long veterinarioId, Long servicioId, String estado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Pageable pageable) {
        Instant desde = fechaDesde != null ? toBusinessInstant(fechaDesde) : null;
        Instant hasta = fechaHasta != null ? toBusinessInstant(fechaHasta) : null;
        return citaRepositoryPort.findAll(mascotaId, veterinarioId, servicioId, estado, desde, hasta, pageable);
    }

    public Page<Cita> listarPorMascota(Long mascotaId, Pageable pageable) {
        return citaRepositoryPort.findByMascotaId(mascotaId, pageable);
    }

    public Page<Cita> listarPorVeterinario(Long veterinarioId, Pageable pageable) {
        return citaRepositoryPort.findByVeterinarioId(veterinarioId, pageable);
    }

    public List<String> obtenerSlotsDisponibles(Long veterinarioId, LocalDate fecha, Long servicioId) {
        Servicio servicio = servicioRepositoryPort.findById(servicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        int diaSemana = fecha.getDayOfWeek().getValue();
        List<DisponibilidadVeterinario> disponibilidades = disponibilidadRepositoryPort
                .findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemana);

        if (disponibilidades.isEmpty()) {
            return List.of();
        }

        Instant inicioDia = fecha.atStartOfDay(BUSINESS_ZONE).toInstant();
        Instant finDia = fecha.plusDays(1).atStartOfDay(BUSINESS_ZONE).toInstant();

        List<BloqueoVeterinario> bloqueos = bloqueoRepositoryPort
                .findByVeterinarioIdAndFecha(veterinarioId, fecha);

        List<Cita> citasExistentes = citaRepositoryPort
                .findByVeterinarioIdAndFechaHoraBetween(veterinarioId, inicioDia, finDia);

        List<String> horarios = new java.util.ArrayList<>();

        for (DisponibilidadVeterinario disp : disponibilidades) {
            if (!disp.getActivo()) continue;

            LocalTime slotInicio = disp.getHoraInicio();
            int duracion = servicio.getDuracionMinutos();

            while (!slotInicio.isAfter(disp.getHoraFin().minusMinutes(duracion))) {
                LocalTime slotFin = slotInicio.plusMinutes(duracion);
                boolean bloqueado = false;

                for (BloqueoVeterinario bloq : bloqueos) {
                    if (!slotInicio.isBefore(bloq.getHoraFin()) &&
                            !bloq.getHoraInicio().isBefore(slotFin)) {
                        continue;
                    }
                    if (slotInicio.isBefore(bloq.getHoraFin()) &&
                            bloq.getHoraInicio().isBefore(slotFin)) {
                        bloqueado = true;
                        break;
                    }
                }

                if (!bloqueado) {
                    for (Cita existente : citasExistentes) {
                        if ("CANCELADA".equals(existente.getEstado()) || "NO_ASISTIO".equals(existente.getEstado())) {
                            continue;
                        }
                        LocalTime extInicio = existente.getFechaHora().atZone(BUSINESS_ZONE).toLocalTime();
                        LocalTime extFin = extInicio.plusMinutes(existente.getServicio().getDuracionMinutos());
                        if (slotInicio.isBefore(extFin) && extInicio.isBefore(slotFin)) {
                            bloqueado = true;
                            break;
                        }
                    }
                }

                if (!bloqueado) {
                    horarios.add(slotInicio.toString());
                }

                slotInicio = slotInicio.plusMinutes(duracion);
            }
        }

        return horarios;
    }

    private void validarDisponibilidadYHorarios(Long veterinarioId, Servicio servicio, Instant fechaHora, Long citaAExcluirId) {
        LocalDateTime fechaHoraLocal = LocalDateTime.ofInstant(fechaHora, BUSINESS_ZONE);
        LocalTime horaInicioCita = fechaHoraLocal.toLocalTime();
        LocalTime horaFinCita = horaInicioCita.plusMinutes(servicio.getDuracionMinutos());
        LocalDate fecha = fechaHoraLocal.toLocalDate();
        int diaSemanaVal = fechaHoraLocal.getDayOfWeek().getValue();

        List<DisponibilidadVeterinario> disponibilidades = disponibilidadRepositoryPort.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemanaVal);
        boolean dentroDeHorario = false;
        for (DisponibilidadVeterinario disp : disponibilidades) {
            if (disp.getActivo()) {
                if (!horaInicioCita.isBefore(disp.getHoraInicio()) && !horaFinCita.isAfter(disp.getHoraFin())) {
                    dentroDeHorario = true;
                    break;
                }
            }
        }
        if (!dentroDeHorario) {
            throw new ScheduleConflictException("La fecha u hora seleccionada está fuera del horario disponible del veterinario");
        }

        Instant inicioDia = fecha.atStartOfDay(BUSINESS_ZONE).toInstant();
        Instant finDia = fecha.plusDays(1).atStartOfDay(BUSINESS_ZONE).toInstant();

        List<BloqueoVeterinario> bloqueos = bloqueoRepositoryPort.findByVeterinarioIdAndFecha(veterinarioId, fecha);
        for (BloqueoVeterinario bloq : bloqueos) {
            if (!horaInicioCita.isBefore(bloq.getHoraFin()) &&
                    !bloq.getHoraInicio().isBefore(horaFinCita)) {
                continue;
            }
            if (horaInicioCita.isBefore(bloq.getHoraFin()) &&
                    bloq.getHoraInicio().isBefore(horaFinCita)) {
                throw new ScheduleConflictException("El veterinario tiene un bloqueo de horario en la hora seleccionada: " + bloq.getMotivo());
            }
        }

        List<Cita> citasDelDia = citaRepositoryPort.findByVeterinarioIdAndFechaHoraBetween(veterinarioId, inicioDia, finDia);

        for (Cita existente : citasDelDia) {
            if (citaAExcluirId != null && existente.getId().equals(citaAExcluirId)) {
                continue;
            }
            if ("CANCELADA".equals(existente.getEstado()) || "NO_ASISTIO".equals(existente.getEstado())) {
                continue;
            }

            LocalTime extInicio = existente.getFechaHora().atZone(BUSINESS_ZONE).toLocalTime();
            LocalTime extFin = extInicio.plusMinutes(existente.getServicio().getDuracionMinutos());

            if (horaInicioCita.isBefore(extFin) && extInicio.isBefore(horaFinCita)) {
                throw new ScheduleConflictException("El veterinario ya tiene otra cita programada en este horario (" + existente.getMascota().getNombre() + " a las " + extInicio + ")");
            }
        }
    }

    private Instant toBusinessInstant(LocalDateTime fechaHoraLocal) {
        return fechaHoraLocal.atZone(BUSINESS_ZONE).toInstant();
    }
}
