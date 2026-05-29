package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.MascotaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class MascotaSpecification {

    public static Specification<MascotaEntity> conFiltros(
            String nombre, String especie, String raza, String sexo,
            Boolean activo, Long duenoId) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (nombre != null && !nombre.isBlank())
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            if (especie != null && !especie.isBlank())
                predicates.add(cb.like(cb.lower(root.get("especie")), "%" + especie.toLowerCase() + "%"));
            if (raza != null && !raza.isBlank())
                predicates.add(cb.like(cb.lower(root.get("raza")), "%" + raza.toLowerCase() + "%"));
            if (sexo != null && !sexo.isBlank())
                predicates.add(cb.equal(root.get("sexo"), sexo));
            if (activo != null)
                predicates.add(cb.equal(root.get("activo"), activo));
            if (duenoId != null) {
                var join = root.join("responsables");
                predicates.add(cb.equal(join.get("dueno").get("id"), duenoId));
                query.distinct(true);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
