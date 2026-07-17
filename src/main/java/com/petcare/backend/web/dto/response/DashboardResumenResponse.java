package com.petcare.backend.web.dto.response;

import java.util.List;

public record DashboardResumenResponse(
        long citasHoy,
        long pacientesEnEspera,
        long atencionesCompletadas,
        long vacunasProximas,
        long cancelacionesHoy,
        List<ServicioTopItem> serviciosTop,
        List<CitasPorEstadoItem> citasPorEstado
) {
    public record ServicioTopItem(String nombre, long cantidad) {}
    public record CitasPorEstadoItem(String estado, long cantidad) {}
}
