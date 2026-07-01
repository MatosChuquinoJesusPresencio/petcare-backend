package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.UsuarioEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class UsuarioSpecification {

    public static Specification<UsuarioEntity> conFiltros(Boolean soloActivos, String rol) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (soloActivos != null) {
                predicates.add(cb.equal(root.get("activo"), soloActivos));
            }
            if (rol != null && !rol.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("rol")), rol.trim().toUpperCase()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
