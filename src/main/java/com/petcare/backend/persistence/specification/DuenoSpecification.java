package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.DuenoEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class DuenoSpecification {

    public static Specification<DuenoEntity> conFiltros(Boolean soloActivos, String nombre, String dni) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (soloActivos != null)
                predicates.add(cb.equal(root.get("activo"), soloActivos));
            if (nombre != null && !nombre.isBlank())
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            if (dni != null && !dni.isBlank())
                predicates.add(cb.like(cb.lower(root.get("dni")), "%" + dni.toLowerCase() + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
