package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.CitaEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class CitaSpecification {

    public static Specification<CitaEntity> withFilters(Long mascotaId, Long veterinarioId, Long servicioId,
                                                        String estado, Instant fechaDesde, Instant fechaHasta) {
        return Specification
                .where(mascotaIdEquals(mascotaId))
                .and(veterinarioIdEquals(veterinarioId))
                .and(servicioIdEquals(servicioId))
                .and(estadoEquals(estado))
                .and(fechaDesdeGe(fechaDesde))
                .and(fechaHastaLe(fechaHasta));
    }

    private static Specification<CitaEntity> mascotaIdEquals(Long mascotaId) {
        return (Root<CitaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (mascotaId == null) return null;
            return cb.equal(root.get("mascota").get("id"), mascotaId);
        };
    }

    private static Specification<CitaEntity> veterinarioIdEquals(Long veterinarioId) {
        return (Root<CitaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (veterinarioId == null) return null;
            return cb.equal(root.get("veterinario").get("id"), veterinarioId);
        };
    }

    private static Specification<CitaEntity> servicioIdEquals(Long servicioId) {
        return (Root<CitaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (servicioId == null) return null;
            return cb.equal(root.get("servicio").get("id"), servicioId);
        };
    }

    private static Specification<CitaEntity> estadoEquals(String estado) {
        return (Root<CitaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (estado == null) return null;
            return cb.equal(root.get("estado"), estado);
        };
    }

    private static Specification<CitaEntity> fechaDesdeGe(Instant fechaDesde) {
        return (Root<CitaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (fechaDesde == null) return null;
            return cb.greaterThanOrEqualTo(root.get("fechaHora"), fechaDesde);
        };
    }

    private static Specification<CitaEntity> fechaHastaLe(Instant fechaHasta) {
        return (Root<CitaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (fechaHasta == null) return null;
            return cb.lessThanOrEqualTo(root.get("fechaHora"), fechaHasta);
        };
    }
}
