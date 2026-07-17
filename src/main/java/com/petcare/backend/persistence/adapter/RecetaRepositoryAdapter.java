package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Receta;
import com.petcare.backend.domain.port.RecetaRepositoryPort;
import com.petcare.backend.persistence.entity.RecetaDetalleEntity;
import com.petcare.backend.persistence.entity.RecetaEntity;
import com.petcare.backend.persistence.mapper.RecetaMapper;
import com.petcare.backend.persistence.repository.AtencionClinicaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.RecetaJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RecetaRepositoryAdapter implements RecetaRepositoryPort {

    private final RecetaJpaRepository jpaRepository;
    private final RecetaMapper mapper;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final AtencionClinicaJpaRepository atencionClinicaJpaRepository;

    public RecetaRepositoryAdapter(RecetaJpaRepository jpaRepository,
                                    RecetaMapper mapper,
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
    public Optional<Receta> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomainWithDetalles);
    }

    @Override
    public List<Receta> findByAtencionClinicaId(Long atencionClinicaId) {
        return jpaRepository.findByAtencionClinicaId(atencionClinicaId)
                .stream().map(this::toDomainWithDetalles).toList();
    }

    @Override
    public List<Receta> findByMascotaId(Long mascotaId) {
        return jpaRepository.findByMascotaIdOrderByCreadoEnDesc(mascotaId)
                .stream().map(this::toDomainWithDetalles).toList();
    }

    @Override
    public Receta save(Receta receta) {
        RecetaEntity entity = mapper.toEntity(receta);
        if (receta.getMascota() != null && receta.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(receta.getMascota().getId()));
        }
        if (receta.getVeterinario() != null && receta.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(receta.getVeterinario().getId()));
        }
        if (receta.getAtencionClinicaId() != null) {
            entity.setAtencionClinica(atencionClinicaJpaRepository.getReferenceById(receta.getAtencionClinicaId()));
        }
        if (receta.getCreadoPor() != null && receta.getCreadoPor().getId() != null) {
            entity.setCreadoPor(usuarioJpaRepository.getReferenceById(receta.getCreadoPor().getId()));
        }

        if (receta.getDetalles() != null) {
            entity.setDetalles(new ArrayList<>());
            for (var det : receta.getDetalles()) {
                RecetaDetalleEntity detEntity = new RecetaDetalleEntity();
                detEntity.setMedicamento(det.getMedicamento());
                detEntity.setPresentacion(det.getPresentacion());
                detEntity.setDosis(det.getDosis());
                detEntity.setFrecuencia(det.getFrecuencia());
                detEntity.setDuracion(det.getDuracion());
                detEntity.setViaAdministracion(det.getViaAdministracion());
                detEntity.setIndicaciones(det.getIndicaciones());
                detEntity.setReceta(entity);
                entity.getDetalles().add(detEntity);
            }
        }

        RecetaEntity saved = jpaRepository.save(entity);
        return toDomainWithDetalles(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private Receta toDomainWithDetalles(RecetaEntity entity) {
        Receta receta = mapper.toDomain(entity);
        if (entity.getDetalles() != null) {
            receta.setDetalles(entity.getDetalles().stream().map(d -> {
                var det = new com.petcare.backend.domain.model.RecetaDetalle();
                det.setId(d.getId());
                det.setMedicamento(d.getMedicamento());
                det.setPresentacion(d.getPresentacion());
                det.setDosis(d.getDosis());
                det.setFrecuencia(d.getFrecuencia());
                det.setDuracion(d.getDuracion());
                det.setViaAdministracion(d.getViaAdministracion());
                det.setIndicaciones(d.getIndicaciones());
                return det;
            }).toList());
        }
        return receta;
    }
}
