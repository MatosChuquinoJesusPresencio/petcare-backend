package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.ServicioEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ServicioSpecification {

    public static Specification<ServicioEntity> withFilters(Boolean soloActivos, String nombre) {
        return Specification
                .where(soloActivosFilter(soloActivos))
                .and(nombreLike(nombre));
    }

    private static Specification<ServicioEntity> soloActivosFilter(Boolean soloActivos) {
        return (Root<ServicioEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (soloActivos == null || !soloActivos) return null;
            return cb.isTrue(root.get("activo"));
        };
    }

    private static Specification<ServicioEntity> nombreLike(String nombre) {
        return (Root<ServicioEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (nombre == null) return null;
            return cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        };
    }
}
