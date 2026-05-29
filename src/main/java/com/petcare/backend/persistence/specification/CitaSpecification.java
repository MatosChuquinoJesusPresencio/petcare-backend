package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.CitaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CitaSpecification {

    public static Specification<CitaEntity> conFiltros(
            Long mascotaId, Long veterinarioId, Long servicioId,
            String estado, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (mascotaId != null)
                predicates.add(cb.equal(root.get("mascota").get("id"), mascotaId));
            if (veterinarioId != null)
                predicates.add(cb.equal(root.get("veterinario").get("id"), veterinarioId));
            if (servicioId != null)
                predicates.add(cb.equal(root.get("servicio").get("id"), servicioId));
            if (estado != null && !estado.isBlank())
                predicates.add(cb.equal(root.get("estado"), estado));
            if (fechaDesde != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaHora"), fechaDesde));
            if (fechaHasta != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaHora"), fechaHasta));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
