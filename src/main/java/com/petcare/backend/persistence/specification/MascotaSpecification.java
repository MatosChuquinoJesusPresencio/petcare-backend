package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.MascotaEntity;
import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class MascotaSpecification {

    public static Specification<MascotaEntity> withFilters(String nombre, String especie, String raza,
                                                           String genero, Boolean estado, Long duenoId) {
        return Specification
                .where(nombreLike(nombre))
                .and(especieLike(especie))
                .and(razaLike(raza))
                .and(generoEquals(genero))
                .and(estadoEquals(estado))
                .and(duenoIdEquals(duenoId));
    }

    private static Specification<MascotaEntity> nombreLike(String nombre) {
        return (Root<MascotaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (nombre == null) return null;
            return cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        };
    }

    private static Specification<MascotaEntity> especieLike(String especie) {
        return (Root<MascotaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (especie == null) return null;
            return cb.like(cb.lower(root.get("especie")), "%" + especie.toLowerCase() + "%");
        };
    }

    private static Specification<MascotaEntity> razaLike(String raza) {
        return (Root<MascotaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (raza == null) return null;
            return cb.like(cb.lower(root.get("raza")), "%" + raza.toLowerCase() + "%");
        };
    }

    private static Specification<MascotaEntity> generoEquals(String genero) {
        return (Root<MascotaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (genero == null) return null;
            return cb.equal(root.get("genero"), genero);
        };
    }

    private static Specification<MascotaEntity> estadoEquals(Boolean estado) {
        return (Root<MascotaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (estado == null) return null;
            return cb.equal(root.get("estado"), estado);
        };
    }

    private static Specification<MascotaEntity> duenoIdEquals(Long duenoId) {
        return (Root<MascotaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (duenoId == null) return null;

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<MascotaResponsableEntity> mr = subquery.from(MascotaResponsableEntity.class);
            subquery.select(mr.get("id"))
                    .where(cb.and(
                            cb.equal(mr.get("mascota").get("id"), root.get("id")),
                            cb.equal(mr.get("dueno").get("id"), duenoId)
                    ));
            return cb.exists(subquery);
        };
    }
}
