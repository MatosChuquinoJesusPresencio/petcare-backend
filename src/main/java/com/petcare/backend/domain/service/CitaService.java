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
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ScheduleConflictException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepositoryPort citaRepositoryPort;
    private final BloqueoVeterinarioRepositoryPort bloqueoRepositoryPort;
    private final DisponibilidadVeterinarioRepositoryPort disponibilidadRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final ServicioRepositoryPort servicioRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public CitaService(CitaRepositoryPort citaRepositoryPort,
                       BloqueoVeterinarioRepositoryPort bloqueoRepositoryPort,
                       DisponibilidadVeterinarioRepositoryPort disponibilidadRepositoryPort,
                       MascotaRepositoryPort mascotaRepositoryPort,
                       ServicioRepositoryPort servicioRepositoryPort,
                       UsuarioRepositoryPort usuarioRepositoryPort) {
        this.citaRepositoryPort = citaRepositoryPort;
        this.bloqueoRepositoryPort = bloqueoRepositoryPort;
        this.disponibilidadRepositoryPort = disponibilidadRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.servicioRepositoryPort = servicioRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public Cita agendarCita(Long mascotaId, Long veterinarioId, Long servicioId, LocalDateTime fechaHora, String notas, Long creadoPorId) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));
        
        if (!"VETERINARIO".equalsIgnoreCase(veterinario.getRol())) {
            throw new BusinessRuleException("The assigned user does not have the VETERINARIAN role");
        }

        Servicio servicio = servicioRepositoryPort.findById(servicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        
        if (!servicio.getActivo()) {
            throw new BusinessRuleException("The selected service is not active");
        }

        Usuario creadoPor = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found"));

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

        return citaRepositoryPort.save(cita);
    }

    @Transactional
    public Cita reprogramarCita(Long citaId, LocalDateTime nuevaFechaHora) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if ("CANCELADA".equals(cita.getEstado()) || "ATENDIDA".equals(cita.getEstado())) {
            throw new BusinessRuleException("Cannot reschedule a cancelled or completed appointment");
        }

        validarDisponibilidadYHorarios(cita.getVeterinario().getId(), cita.getServicio(), nuevaFechaHora, citaId);

        cita.setFechaHora(nuevaFechaHora);
        cita.setEstado("REPROGRAMADA");
        return citaRepositoryPort.save(cita);
    }

    public Cita cambiarEstadoCita(Long citaId, String nuevoEstado) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        String estadoUpper = nuevoEstado.toUpperCase();
        if (!List.of("AGENDADA", "CONFIRMADA", "REPROGRAMADA", "CANCELADA", "ATENDIDA", "NO_ASISTIO").contains(estadoUpper)) {
            throw new BusinessRuleException("Invalid appointment status");
        }

        cita.setEstado(estadoUpper);
        return citaRepositoryPort.save(cita);
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepositoryPort.findById(id);
    }

    public Page<Cita> listarTodas(Pageable pageable) {
        return citaRepositoryPort.findAll(pageable);
    }

    public Page<Cita> listarPorMascota(Long mascotaId, Pageable pageable) {
        return citaRepositoryPort.findByMascotaId(mascotaId, pageable);
    }

    public Page<Cita> listarPorVeterinario(Long veterinarioId, Pageable pageable) {
        return citaRepositoryPort.findByVeterinarioId(veterinarioId, pageable);
    }

    private void validarDisponibilidadYHorarios(Long veterinarioId, Servicio servicio, LocalDateTime fechaHora, Long citaAExcluirId) {
        LocalTime horaInicioCita = fechaHora.toLocalTime();
        LocalTime horaFinCita = horaInicioCita.plusMinutes(servicio.getDuracionMinutos());
        int diaSemanaVal = fechaHora.getDayOfWeek().getValue(); // 1 = Lunes, 7 = Domingo

        List<DisponibilidadVeterinario> disponibilidades = disponibilidadRepositoryPort.findByVeterinarioIdAndDiaSemana(veterinarioId, diaSemanaVal);
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
            throw new ScheduleConflictException("The selected date or time is outside the veterinarian's available schedule");
        }

        List<BloqueoVeterinario> bloqueos = bloqueoRepositoryPort.findByVeterinarioIdAndFecha(veterinarioId, fechaHora.toLocalDate());
        for (BloqueoVeterinario bloq : bloqueos) {
            if (horaInicioCita.isBefore(bloq.getHoraFin()) && bloq.getHoraInicio().isBefore(horaFinCita)) {
                throw new ScheduleConflictException("The veterinarian has a schedule block at the selected time: " + bloq.getMotivo());
            }
        }

        LocalDateTime inicioDia = fechaHora.toLocalDate().atStartOfDay();
        LocalDateTime finDia = fechaHora.toLocalDate().atTime(23, 59, 59);
        List<Cita> citasDelDia = citaRepositoryPort.findByVeterinarioIdAndFechaHoraBetween(veterinarioId, inicioDia, finDia);

        for (Cita existente : citasDelDia) {
            if (citaAExcluirId != null && existente.getId().equals(citaAExcluirId)) {
                continue;
            }
            if ("CANCELADA".equals(existente.getEstado()) || "NO_ASISTIO".equals(existente.getEstado())) {
                continue;
            }

            LocalTime extInicio = existente.getFechaHora().toLocalTime();
            LocalTime extFin = extInicio.plusMinutes(existente.getServicio().getDuracionMinutos());

            if (horaInicioCita.isBefore(extFin) && extInicio.isBefore(horaFinCita)) {
                throw new ScheduleConflictException("The veterinarian already has another appointment scheduled at this time (" + existente.getMascota().getNombre() + " at " + extInicio + ")");
            }
        }
    }
}
