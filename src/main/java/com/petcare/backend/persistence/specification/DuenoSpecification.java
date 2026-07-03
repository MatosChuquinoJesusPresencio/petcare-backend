package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.DuenoEntity;
import com.petcare.backend.persistence.entity.UsuarioEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class DuenoSpecification {

    public static Specification<DuenoEntity> withFilters(Boolean soloActivos, String nombre, String dni) {
        return Specification
                .where(soloActivosFilter(soloActivos))
                .and(nombreLike(nombre))
                .and(dniEquals(dni));
    }

    private static Specification<DuenoEntity> soloActivosFilter(Boolean soloActivos) {
        return (Root<DuenoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (soloActivos == null || !soloActivos) return null;
            Join<DuenoEntity, UsuarioEntity> usuario = root.join("usuario");
            return cb.isTrue(usuario.get("estado"));
        };
    }

    private static Specification<DuenoEntity> nombreLike(String nombre) {
        return (Root<DuenoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (nombre == null) return null;
            Join<DuenoEntity, UsuarioEntity> usuario = root.join("usuario");
            String pattern = "%" + nombre.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(usuario.get("nombres")), pattern),
                    cb.like(cb.lower(usuario.get("apellidos")), pattern)
            );
        };
    }

    private static Specification<DuenoEntity> dniEquals(String dni) {
        return (Root<DuenoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (dni == null) return null;
            return cb.equal(root.get("dni"), dni);
        };
    }
}
