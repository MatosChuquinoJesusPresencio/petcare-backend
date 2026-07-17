package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.AuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

public interface AuditoriaJpaRepository extends JpaRepository<AuditoriaEntity, Long>, JpaSpecificationExecutor<AuditoriaEntity> {
    List<AuditoriaEntity> findByTablaAfectadaAndUsuarioIdAndFechaCambioBetween(
            String tablaAfectada, Long usuarioId, Instant fechaDesde, Instant fechaHasta);

    List<AuditoriaEntity> findByTablaAfectadaAndFechaCambioBetween(
            String tablaAfectada, Instant fechaDesde, Instant fechaHasta);

    List<AuditoriaEntity> findByUsuarioIdAndFechaCambioBetween(
            Long usuarioId, Instant fechaDesde, Instant fechaHasta);

    List<AuditoriaEntity> findByFechaCambioBetween(Instant fechaDesde, Instant fechaHasta);

    List<AuditoriaEntity> findByTablaAfectada(String tablaAfectada);

    List<AuditoriaEntity> findByUsuarioId(Long usuarioId);
}
