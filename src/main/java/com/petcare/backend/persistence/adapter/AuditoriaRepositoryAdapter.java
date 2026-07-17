package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Auditoria;
import com.petcare.backend.domain.port.AuditoriaRepositoryPort;
import com.petcare.backend.persistence.entity.AuditoriaEntity;
import com.petcare.backend.persistence.mapper.AuditoriaMapper;
import com.petcare.backend.persistence.repository.AuditoriaJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class AuditoriaRepositoryAdapter implements AuditoriaRepositoryPort {

    private final AuditoriaJpaRepository auditoriaJpaRepository;
    private final AuditoriaMapper auditoriaMapper;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public AuditoriaRepositoryAdapter(AuditoriaJpaRepository auditoriaJpaRepository,
                                      AuditoriaMapper auditoriaMapper,
                                      UsuarioJpaRepository usuarioJpaRepository) {
        this.auditoriaJpaRepository = auditoriaJpaRepository;
        this.auditoriaMapper = auditoriaMapper;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Auditoria save(Auditoria auditoria) {
        AuditoriaEntity entity = auditoriaMapper.toEntity(auditoria);
        if (auditoria.getUsuario() != null && auditoria.getUsuario().getId() != null) {
            entity.setUsuario(usuarioJpaRepository.getReferenceById(auditoria.getUsuario().getId()));
        }
        if (entity.getFechaCambio() == null) {
            entity.setFechaCambio(Instant.now());
        }
        AuditoriaEntity saved = auditoriaJpaRepository.save(entity);
        return auditoriaMapper.toDomain(saved);
    }

    @Override
    public List<Auditoria> buscar(String tablaAfectada, Long usuarioId, Instant fechaDesde, Instant fechaHasta) {
        if (tablaAfectada != null && usuarioId != null && fechaDesde != null && fechaHasta != null) {
            return auditoriaJpaRepository
                    .findByTablaAfectadaAndUsuarioIdAndFechaCambioBetween(tablaAfectada, usuarioId, fechaDesde, fechaHasta)
                    .stream().map(auditoriaMapper::toDomain).toList();
        }
        if (tablaAfectada != null && fechaDesde != null && fechaHasta != null) {
            return auditoriaJpaRepository
                    .findByTablaAfectadaAndFechaCambioBetween(tablaAfectada, fechaDesde, fechaHasta)
                    .stream().map(auditoriaMapper::toDomain).toList();
        }
        if (usuarioId != null && fechaDesde != null && fechaHasta != null) {
            return auditoriaJpaRepository
                    .findByUsuarioIdAndFechaCambioBetween(usuarioId, fechaDesde, fechaHasta)
                    .stream().map(auditoriaMapper::toDomain).toList();
        }
        if (fechaDesde != null && fechaHasta != null) {
            return auditoriaJpaRepository
                    .findByFechaCambioBetween(fechaDesde, fechaHasta)
                    .stream().map(auditoriaMapper::toDomain).toList();
        }
        if (tablaAfectada != null) {
            return auditoriaJpaRepository.findByTablaAfectada(tablaAfectada)
                    .stream().map(auditoriaMapper::toDomain).toList();
        }
        if (usuarioId != null) {
            return auditoriaJpaRepository.findByUsuarioId(usuarioId)
                    .stream().map(auditoriaMapper::toDomain).toList();
        }
        return auditoriaJpaRepository.findAll().stream().map(auditoriaMapper::toDomain).toList();
    }
}
