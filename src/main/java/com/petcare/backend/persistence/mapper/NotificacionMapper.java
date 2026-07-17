package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Notificacion;
import com.petcare.backend.persistence.entity.NotificacionEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionEntity toEntity(Notificacion model) {
        if (model == null) return null;
        NotificacionEntity entity = new NotificacionEntity();
        entity.setId(model.getId());
        entity.setTipo(model.getTipo());
        entity.setDestinoUsuarioId(model.getDestinoUsuarioId());
        entity.setMascotaId(model.getMascotaId());
        entity.setCitaId(model.getCitaId());
        entity.setCanal(model.getCanal());
        entity.setMensaje(model.getMensaje());
        entity.setEstado(model.getEstado());
        entity.setFechaEnvio(model.getFechaEnvio());
        entity.setErrorMensaje(model.getErrorMensaje());
        entity.setLeido(model.getLeido());
        return entity;
    }

    public Notificacion toModel(NotificacionEntity entity) {
        if (entity == null) return null;
        Notificacion model = new Notificacion();
        model.setId(entity.getId());
        model.setTipo(entity.getTipo());
        model.setDestinoUsuarioId(entity.getDestinoUsuarioId());
        model.setMascotaId(entity.getMascotaId());
        model.setCitaId(entity.getCitaId());
        model.setCanal(entity.getCanal());
        model.setMensaje(entity.getMensaje());
        model.setEstado(entity.getEstado());
        model.setFechaEnvio(entity.getFechaEnvio());
        model.setErrorMensaje(entity.getErrorMensaje());
        model.setLeido(entity.getLeido());
        model.setCreadoEn(entity.getCreadoEn());
        return model;
    }
}
