package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class ContactoEmergenciaSpecification {

    public static Specification<ContactoEmergenciaEntity> conFiltros(
            Long duenoId, String nombre, String telefono, String relacion) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (duenoId != null)
                predicates.add(cb.equal(root.get("dueno").get("id"), duenoId));
            if (nombre != null && !nombre.isBlank())
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            if (telefono != null && !telefono.isBlank())
                predicates.add(cb.like(root.get("telefono"), "%" + telefono + "%"));
            if (relacion != null && !relacion.isBlank())
                predicates.add(cb.like(cb.lower(root.get("relacion")), "%" + relacion.toLowerCase() + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
