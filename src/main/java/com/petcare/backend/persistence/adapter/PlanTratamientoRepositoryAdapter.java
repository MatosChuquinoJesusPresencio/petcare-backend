package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.PlanTratamiento;
import com.petcare.backend.domain.model.PlanTratamientoActividad;
import com.petcare.backend.domain.port.PlanTratamientoRepositoryPort;
import com.petcare.backend.persistence.entity.PlanTratamientoActividadEntity;
import com.petcare.backend.persistence.entity.PlanTratamientoEntity;
import com.petcare.backend.persistence.mapper.PlanTratamientoMapper;
import com.petcare.backend.persistence.repository.AtencionClinicaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.PlanTratamientoJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PlanTratamientoRepositoryAdapter implements PlanTratamientoRepositoryPort {

    private final PlanTratamientoJpaRepository jpaRepository;
    private final PlanTratamientoMapper mapper;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final AtencionClinicaJpaRepository atencionClinicaJpaRepository;

    public PlanTratamientoRepositoryAdapter(PlanTratamientoJpaRepository jpaRepository,
                                             PlanTratamientoMapper mapper,
                                             MascotaJpaRepository mascotaJpaRepository,
                                             UsuarioJpaRepository usuarioJpaRepository,
                                             AtencionClinicaJpaRepository atencionClinicaJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.atencionClinicaJpaRepository = atencionClinicaJpaRepository;
    }

    @Override
    public Optional<PlanTratamiento> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomainWithActividades);
    }

    @Override
    public List<PlanTratamiento> findByMascotaId(Long mascotaId) {
        return jpaRepository.findByMascotaIdOrderByCreadoEnDesc(mascotaId)
                .stream().map(this::toDomainWithActividades).toList();
    }

    @Override
    public List<PlanTratamiento> findByAtencionClinicaId(Long atencionClinicaId) {
        return jpaRepository.findByAtencionClinicaId(atencionClinicaId)
                .stream().map(this::toDomainWithActividades).toList();
    }

    @Override
    public PlanTratamiento save(PlanTratamiento plan) {
        PlanTratamientoEntity entity = mapper.toEntity(plan);
        if (plan.getMascota() != null && plan.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(plan.getMascota().getId()));
        }
        if (plan.getAtencionClinicaId() != null) {
            entity.setAtencionClinica(atencionClinicaJpaRepository.getReferenceById(plan.getAtencionClinicaId()));
        }
        if (plan.getVeterinario() != null && plan.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(plan.getVeterinario().getId()));
        }
        if (plan.getCreadoPor() != null && plan.getCreadoPor().getId() != null) {
            entity.setCreadoPor(usuarioJpaRepository.getReferenceById(plan.getCreadoPor().getId()));
        }

        if (plan.getActividades() != null) {
            entity.setActividades(new ArrayList<>());
            for (var act : plan.getActividades()) {
                PlanTratamientoActividadEntity actEntity = new PlanTratamientoActividadEntity();
                actEntity.setTipo(act.getTipo());
                actEntity.setDescripcion(act.getDescripcion());
                actEntity.setFechaProgramada(act.getFechaProgramada());
                actEntity.setHoraProgramada(act.getHoraProgramada());
                actEntity.setFrecuencia(act.getFrecuencia());
                actEntity.setResponsable(act.getResponsable());
                actEntity.setEstado(act.getEstado() != null ? act.getEstado() : "PENDIENTE");
                actEntity.setObservaciones(act.getObservaciones());
                actEntity.setPlanTratamiento(entity);
                entity.getActividades().add(actEntity);
            }
        }

        PlanTratamientoEntity saved = jpaRepository.save(entity);
        return toDomainWithActividades(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private PlanTratamiento toDomainWithActividades(PlanTratamientoEntity entity) {
        PlanTratamiento plan = mapper.toDomain(entity);
        if (entity.getActividades() != null) {
            plan.setActividades(entity.getActividades().stream().map(a -> {
                var act = new PlanTratamientoActividad();
                act.setId(a.getId());
                act.setTipo(a.getTipo());
                act.setDescripcion(a.getDescripcion());
                act.setFechaProgramada(a.getFechaProgramada());
                act.setHoraProgramada(a.getHoraProgramada());
                act.setFrecuencia(a.getFrecuencia());
                act.setResponsable(a.getResponsable());
                act.setEstado(a.getEstado());
                act.setObservaciones(a.getObservaciones());
                return act;
            }).toList());
        }
        return plan;
    }
}
