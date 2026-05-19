package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.BloqueoPort;
import com.petcare.backend.domain.port.CitaPort;
import com.petcare.backend.domain.port.DisponibilidadPort;
import com.petcare.backend.domain.port.MascotaPort;
import com.petcare.backend.domain.port.ServicioPort;
import com.petcare.backend.domain.port.UsuarioPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ScheduleConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaPort citaPort;
    private final BloqueoPort bloqueoPort;
    private final DisponibilidadPort disponibilidadPort;
    private final MascotaPort mascotaPort;
    private final ServicioPort servicioPort;
    private final UsuarioPort usuarioPort;

    public CitaService(CitaPort citaPort,
                       BloqueoPort bloqueoPort,
                       DisponibilidadPort disponibilidadPort,
                       MascotaPort mascotaPort,
                       ServicioPort servicioPort,
                       UsuarioPort usuarioPort) {
        this.citaPort = citaPort;
        this.bloqueoPort = bloqueoPort;
        this.disponibilidadPort = disponibilidadPort;
        this.mascotaPort = mascotaPort;
        this.servicioPort = servicioPort;
        this.usuarioPort = usuarioPort;
    }

    @Transactional
    public Cita agendarCita(Long mascotaId, Long veterinarioId, Long servicioId, LocalDateTime fechaHora, String notas, Long creadoPorId) {
        Mascota mascota = mascotaPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        
        Usuario veterinario = usuarioPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        
        if (!"VETERINARIO".equalsIgnoreCase(veterinario.getRol())) {
            throw new BusinessRuleException("El usuario asignado no tiene el rol de VETERINARIO");
        }

        Servicio servicio = servicioPort.findById(servicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        if (!servicio.getActivo()) {
            throw new BusinessRuleException("El servicio seleccionado no está activo");
        }

        Usuario creadoPor = usuarioPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        validarDisponibilidadYHorarios(veterinarioId, servicio, fechaHora, null);

        Cita cita = Cita.builder()
                .mascota(mascota)
                .veterinario(veterinario)
                .servicio(servicio)
                .fechaHora(fechaHora)
                .estado("AGENDADA")
                .notas(notas)
                .creadoPor(creadoPor)
                .build();

        return citaPort.save(cita);
    }

    @Transactional
    public Cita reprogramarCita(Long citaId, LocalDateTime nuevaFechaHora) {
        Cita cita = citaPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if ("CANCELADA".equals(cita.getEstado()) || "ATENDIDA".equals(cita.getEstado())) {
            throw new BusinessRuleException("No se puede reprogramar una cita cancelada o ya atendida");
        }

        validarDisponibilidadYHorarios(cita.getVeterinario().getId(), cita.getServicio(), nuevaFechaHora, citaId);

        cita.setFechaHora(nuevaFechaHora);
        cita.setEstado("REPROGRAMADA");
        return citaPort.save(cita);
    }

    public Cita cambiarEstadoCita(Long citaId, String nuevoEstado) {
        Cita cita = citaPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        String estadoUpper = nuevoEstado.toUpperCase();
        if (!List.of("AGENDADA", "CONFIRMADA", "REPROGRAMADA", "CANCELADA", "ATENDIDA", "NO_ASISTIO").contains(estadoUpper)) {
            throw new BusinessRuleException("Estado de cita no válido");
        }

        cita.setEstado(estadoUpper);
        return citaPort.save(cita);
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return citaPort.findById(id);
    }

    public List<Cita> listarTodas() {
        return citaPort.findAll();
    }

    public List<Cita> listarPorMascota(Long mascotaId) {
        return citaPort.findByMascotaId(mascotaId);
    }

    public List<Cita> listarPorVeterinario(Long veterinarioId) {
        return citaPort.findByVeterinarioId(veterinarioId);
    }

    private void validarDisponibilidadYHorarios(Long veterinarioId, Servicio servicio, LocalDateTime fechaHora, Long citaAExcluirId) {
        LocalTime horaInicioCita = fechaHora.toLocalTime();
        LocalTime horaFinCita = horaInicioCita.plusMinutes(servicio.getDuracionMinutos());
        int diaSemanaVal = fechaHora.getDayOfWeek().getValue();

        List<DisponibilidadVeterinario> disponibilidades = disponibilidadPort.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemanaVal);
        boolean dentroDeHorario = false;
        for (DisponibilidadVeterinario disp : disponibilidades) {
            if (disp.getActivo() &&
                    !horaInicioCita.isBefore(disp.getHoraInicio()) &&
                    !horaFinCita.isAfter(disp.getHoraFin())) {
                dentroDeHorario = true;
                break;
            }
        }
        if (!dentroDeHorario) {
            throw new ScheduleConflictException("La fecha u hora seleccionada está fuera del horario disponible del veterinario");
        }

        List<BloqueoVeterinario> bloqueos = bloqueoPort.findByVeterinarioIdAndFecha(veterinarioId, fechaHora.toLocalDate());
        for (BloqueoVeterinario bloq : bloqueos) {
            if (horaInicioCita.isBefore(bloq.getHoraFin()) && bloq.getHoraInicio().isBefore(horaFinCita)) {
                throw new ScheduleConflictException("El veterinario tiene un bloqueo de agenda programado en el horario seleccionado: " + bloq.getMotivo());
            }
        }

        LocalDateTime inicioDia = fechaHora.toLocalDate().atStartOfDay();
        LocalDateTime finDia = fechaHora.toLocalDate().atTime(23, 59, 59);
        List<Cita> citasDelDia = citaPort.findByVeterinarioIdAndFechaHoraBetween(veterinarioId, inicioDia, finDia);

        for (Cita existente : citasDelDia) {
            if (citaAExcluirId != null && existente.getId().equals(citaAExcluirId)) {
                continue;
            }

            // Excluir citas canceladas o no asistidas
            if ("CANCELADA".equals(existente.getEstado()) || "NO_ASISTIO".equals(existente.getEstado())) {
                continue;
            }

            LocalTime extInicio = existente.getFechaHora().toLocalTime();
            LocalTime extFin = extInicio.plusMinutes(existente.getServicio().getDuracionMinutos());

            if (horaInicioCita.isBefore(extFin) && extInicio.isBefore(horaFinCita)) {
                throw new ScheduleConflictException("El veterinario ya tiene otra cita agendada en este horario (" + existente.getMascota().getNombre() + " a las " + extInicio + ")");
            }
        }
    }
}
