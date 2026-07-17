package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.port.*;
import com.petcare.backend.web.dto.response.DashboardResumenResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final CitaRepositoryPort citaRepositoryPort;
    private final SalaEsperaRepositoryPort salaEsperaRepositoryPort;
    private final HistorialVacunacionRepositoryPort vacunacionRepositoryPort;

    public DashboardService(CitaRepositoryPort citaRepositoryPort,
                             SalaEsperaRepositoryPort salaEsperaRepositoryPort,
                             HistorialVacunacionRepositoryPort vacunacionRepositoryPort) {
        this.citaRepositoryPort = citaRepositoryPort;
        this.salaEsperaRepositoryPort = salaEsperaRepositoryPort;
        this.vacunacionRepositoryPort = vacunacionRepositoryPort;
    }

    public DashboardResumenResponse obtenerResumen() {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate hoy = LocalDate.now();
        Instant inicioDia = hoy.atStartOfDay(zone).toInstant();
        Instant finDia = hoy.plusDays(1).atStartOfDay(zone).toInstant();

        List<Cita> todasLasCitas = citaRepositoryPort.findAll(PageRequest.of(0, 10000)).getContent();

        long citasHoy = todasLasCitas.stream()
                .filter(c -> c.getFechaHora().isAfter(inicioDia) && c.getFechaHora().isBefore(finDia))
                .count();

        long cancelacionesHoy = todasLasCitas.stream()
                .filter(c -> c.getFechaHora().isAfter(inicioDia) && c.getFechaHora().isBefore(finDia))
                .filter(c -> "CANCELADA".equals(c.getEstado()))
                .count();

        Map<String, Long> citasPorEstado = todasLasCitas.stream()
                .filter(c -> c.getFechaHora().isAfter(inicioDia) && c.getFechaHora().isBefore(finDia))
                .collect(Collectors.groupingBy(Cita::getEstado, Collectors.counting()));

        List<DashboardResumenResponse.CitasPorEstadoItem> estadoItems = citasPorEstado.entrySet().stream()
                .map(e -> new DashboardResumenResponse.CitasPorEstadoItem(e.getKey(), e.getValue()))
                .toList();

        long pacientesEnEspera = salaEsperaRepositoryPort
                .findByEstadoOrderByFechaLlegadaAsc("PENDIENTE", PageRequest.of(0, 1000))
                .getTotalElements();

        long atencionesCompletadas = todasLasCitas.stream()
                .filter(c -> c.getFechaHora().isAfter(inicioDia) && c.getFechaHora().isBefore(finDia))
                .filter(c -> "ATENDIDA".equals(c.getEstado()))
                .count();

        LocalDate enUnaSemana = hoy.plusDays(7);
        long vacunasProximas = vacunacionRepositoryPort
                .findByProximaDosisBetween(hoy, enUnaSemana).size();

        Map<String, Long> serviciosPorCantidad = todasLasCitas.stream()
                .filter(c -> c.getFechaHora().isAfter(inicioDia.minusSeconds(30L * 24 * 60 * 60)) && c.getFechaHora().isBefore(finDia))
                .filter(c -> c.getServicio() != null)
                .collect(Collectors.groupingBy(
                        c -> c.getServicio().getNombre(),
                        Collectors.counting()));

        List<DashboardResumenResponse.ServicioTopItem> serviciosTop = serviciosPorCantidad.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new DashboardResumenResponse.ServicioTopItem(e.getKey(), e.getValue()))
                .toList();

        return new DashboardResumenResponse(
                citasHoy, pacientesEnEspera, atencionesCompletadas,
                vacunasProximas, cancelacionesHoy, serviciosTop, estadoItems
        );
    }
}
