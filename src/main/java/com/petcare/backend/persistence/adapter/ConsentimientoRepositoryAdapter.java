package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Consentimiento;
import com.petcare.backend.domain.port.ConsentimientoRepositoryPort;
import com.petcare.backend.persistence.entity.ConsentimientoEntity;
import com.petcare.backend.persistence.repository.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class ConsentimientoRepositoryAdapter implements ConsentimientoRepositoryPort {

    private final ConsentimientoJpaRepository jpaRepository;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final DuenoJpaRepository duenoJpaRepository;
    private final AtencionClinicaJpaRepository atencionClinicaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public ConsentimientoRepositoryAdapter(ConsentimientoJpaRepository jpaRepository,
                                            MascotaJpaRepository mascotaJpaRepository,
                                            DuenoJpaRepository duenoJpaRepository,
                                            AtencionClinicaJpaRepository atencionClinicaJpaRepository,
                                            UsuarioJpaRepository usuarioJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.duenoJpaRepository = duenoJpaRepository;
        this.atencionClinicaJpaRepository = atencionClinicaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<Consentimiento> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Consentimiento> findByMascotaId(Long mascotaId) {
        return jpaRepository.findByMascotaIdOrderByCreadoEnDesc(mascotaId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Consentimiento> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Consentimiento save(Consentimiento c) {
        ConsentimientoEntity entity = new ConsentimientoEntity();
        if (c.getId() != null) entity.setId(c.getId());
        if (c.getMascota() != null && c.getMascota().getId() != null) entity.setMascota(mascotaJpaRepository.getReferenceById(c.getMascota().getId()));
        if (c.getDuenoId() != null) entity.setDueno(duenoJpaRepository.getReferenceById(c.getDuenoId()));
        if (c.getAtencionClinicaId() != null) entity.setAtencionClinica(atencionClinicaJpaRepository.getReferenceById(c.getAtencionClinicaId()));
        if (c.getVeterinario() != null && c.getVeterinario().getId() != null) entity.setVeterinario(usuarioJpaRepository.getReferenceById(c.getVeterinario().getId()));
        entity.setTipoProcedimiento(c.getTipoProcedimiento());
        entity.setDescripcionProcedimiento(c.getDescripcionProcedimiento());
        entity.setRiesgosDescritos(c.getRiesgosDescritos());
        entity.setAlternativas(c.getAlternativas());
        entity.setConsentido(c.getConsentido());
        entity.setDuenoNombreVerificado(c.getDuenoNombreVerificado());
        entity.setTestigoNombre(c.getTestigoNombre());
        entity.setObservaciones(c.getObservaciones());
        if (c.getCreadoPor() != null && c.getCreadoPor().getId() != null) entity.setCreadoPor(usuarioJpaRepository.getReferenceById(c.getCreadoPor().getId()));
        ConsentimientoEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private Consentimiento toDomain(ConsentimientoEntity e) {
        Consentimiento c = new Consentimiento();
        c.setId(e.getId());
        if (e.getMascota() != null) {
            var m = new com.petcare.backend.domain.model.Mascota();
            m.setId(e.getMascota().getId());
            m.setNombre(e.getMascota().getNombre());
            c.setMascota(m);
        }
        if (e.getDueno() != null) c.setDuenoId(e.getDueno().getId());
        if (e.getAtencionClinica() != null) c.setAtencionClinicaId(e.getAtencionClinica().getId());
        if (e.getVeterinario() != null) {
            var v = new com.petcare.backend.domain.model.Usuario();
            v.setId(e.getVeterinario().getId());
            v.setNombres(e.getVeterinario().getNombres());
            v.setApellidos(e.getVeterinario().getApellidos());
            c.setVeterinario(v);
        }
        c.setTipoProcedimiento(e.getTipoProcedimiento());
        c.setDescripcionProcedimiento(e.getDescripcionProcedimiento());
        c.setRiesgosDescritos(e.getRiesgosDescritos());
        c.setAlternativas(e.getAlternativas());
        c.setConsentido(e.getConsentido());
        c.setFechaConsentimiento(e.getFechaConsentimiento());
        c.setDuenoNombreVerificado(e.getDuenoNombreVerificado());
        c.setTestigoNombre(e.getTestigoNombre());
        c.setObservaciones(e.getObservaciones());
        if (e.getCreadoPor() != null) {
            var u = new com.petcare.backend.domain.model.Usuario();
            u.setId(e.getCreadoPor().getId());
            c.setCreadoPor(u);
        }
        return c;
    }
}
