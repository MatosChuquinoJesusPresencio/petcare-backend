package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Seguimiento;
import com.petcare.backend.domain.port.SeguimientoRepositoryPort;
import com.petcare.backend.persistence.entity.SeguimientoEntity;
import com.petcare.backend.persistence.repository.*;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class SeguimientoRepositoryAdapter implements SeguimientoRepositoryPort {

    private final SeguimientoJpaRepository jpaRepository;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final AtencionClinicaJpaRepository atencionClinicaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final DuenoJpaRepository duenoJpaRepository;

    public SeguimientoRepositoryAdapter(SeguimientoJpaRepository jpaRepository,
                                         MascotaJpaRepository mascotaJpaRepository,
                                         AtencionClinicaJpaRepository atencionClinicaJpaRepository,
                                         UsuarioJpaRepository usuarioJpaRepository,
                                         DuenoJpaRepository duenoJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.atencionClinicaJpaRepository = atencionClinicaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.duenoJpaRepository = duenoJpaRepository;
    }

    @Override
    public Optional<Seguimiento> findById(Long id) { return jpaRepository.findById(id).map(this::toDomain); }

    @Override
    public List<Seguimiento> findByAtencionClinicaId(Long atencionClinicaId) {
        return jpaRepository.findByAtencionClinicaIdOrderByCreadoEnDesc(atencionClinicaId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Seguimiento> findByMascotaId(Long mascotaId) {
        return jpaRepository.findByMascotaIdOrderByCreadoEnDesc(mascotaId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Seguimiento> findByFechaProgramadaBetween(Instant desde, Instant hasta) {
        return jpaRepository.findByFechaProgramadaBetweenOrderByFechaProgramadaAsc(desde, hasta).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Seguimiento> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Seguimiento save(Seguimiento s) {
        SeguimientoEntity entity;
        if (s.getId() != null) {
            entity = jpaRepository.findById(s.getId()).orElse(new SeguimientoEntity());
        } else {
            entity = new SeguimientoEntity();
        }
        if (s.getAtencionClinicaId() != null) entity.setAtencionClinica(atencionClinicaJpaRepository.getReferenceById(s.getAtencionClinicaId()));
        if (s.getMascota() != null && s.getMascota().getId() != null) entity.setMascota(mascotaJpaRepository.getReferenceById(s.getMascota().getId()));
        if (s.getVeterinario() != null && s.getVeterinario().getId() != null) entity.setVeterinario(usuarioJpaRepository.getReferenceById(s.getVeterinario().getId()));
        if (s.getDuenoNotificadoId() != null) entity.setDuenoNotificado(duenoJpaRepository.getReferenceById(s.getDuenoNotificadoId()));
        entity.setTipo(s.getTipo());
        entity.setFechaProgramada(s.getFechaProgramada());
        entity.setFechaCompletada(s.getFechaCompletada());
        entity.setMotivo(s.getMotivo());
        entity.setResultado(s.getResultado());
        entity.setEstado(s.getEstado());
        if (s.getCreadoPor() != null && s.getCreadoPor().getId() != null) entity.setCreadoPor(usuarioJpaRepository.getReferenceById(s.getCreadoPor().getId()));
        SeguimientoEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private Seguimiento toDomain(SeguimientoEntity e) {
        Seguimiento s = new Seguimiento();
        s.setId(e.getId());
        if (e.getAtencionClinica() != null) s.setAtencionClinicaId(e.getAtencionClinica().getId());
        if (e.getMascota() != null) {
            var m = new com.petcare.backend.domain.model.Mascota();
            m.setId(e.getMascota().getId());
            m.setNombre(e.getMascota().getNombre());
            s.setMascota(m);
        }
        if (e.getVeterinario() != null) {
            var v = new com.petcare.backend.domain.model.Usuario();
            v.setId(e.getVeterinario().getId());
            v.setNombres(e.getVeterinario().getNombres());
            v.setApellidos(e.getVeterinario().getApellidos());
            s.setVeterinario(v);
        }
        if (e.getDuenoNotificado() != null) s.setDuenoNotificadoId(e.getDuenoNotificado().getId());
        s.setTipo(e.getTipo());
        s.setFechaProgramada(e.getFechaProgramada());
        s.setFechaCompletada(e.getFechaCompletada());
        s.setMotivo(e.getMotivo());
        s.setResultado(e.getResultado());
        s.setEstado(e.getEstado());
        if (e.getCreadoPor() != null) {
            var u = new com.petcare.backend.domain.model.Usuario();
            u.setId(e.getCreadoPor().getId());
            s.setCreadoPor(u);
        }
        s.setCreadoEn(e.getCreadoEn());
        return s;
    }
}
