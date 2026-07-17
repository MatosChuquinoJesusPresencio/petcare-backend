package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Notificacion;
import com.petcare.backend.domain.port.NotificacionRepositoryPort;
import com.petcare.backend.persistence.entity.NotificacionEntity;
import com.petcare.backend.persistence.mapper.NotificacionMapper;
import com.petcare.backend.persistence.repository.NotificacionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificacionRepositoryAdapter implements NotificacionRepositoryPort {

    private final NotificacionJpaRepository repo;
    private final NotificacionMapper mapper;

    public NotificacionRepositoryAdapter(NotificacionJpaRepository repo, NotificacionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Notificacion save(Notificacion notificacion) {
        NotificacionEntity entity = mapper.toEntity(notificacion);
        return mapper.toModel(repo.save(entity));
    }

    @Override
    public List<Notificacion> findByDestinoUsuarioId(Long usuarioId) {
        return repo.findByDestinoUsuarioIdOrderByCreadoEnDesc(usuarioId)
                .stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Notificacion> findAllByOrderByCreadoEnDesc() {
        return repo.findAllByOrderByCreadoEnDesc()
                .stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public long countNoLeidasByDestinoUsuarioId(Long usuarioId) {
        return repo.countByDestinoUsuarioIdAndLeidoFalse(usuarioId);
    }

    @Override
    public void marcarComoLeidas(Long usuarioId) {
        repo.marcarComoLeidas(usuarioId);
    }
}
