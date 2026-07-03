package com.petcare.backend.persistence.specification;

import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ContactoEmergenciaSpecification {

    public static Specification<ContactoEmergenciaEntity> withFilters(Long duenoId, String nombre,
                                                                       String telefono, String relacion) {
        return Specification
                .where(duenoIdEquals(duenoId))
                .and(nombreLike(nombre))
                .and(telefonoEquals(telefono))
                .and(relacionLike(relacion));
    }

    private static Specification<ContactoEmergenciaEntity> duenoIdEquals(Long duenoId) {
        return (Root<ContactoEmergenciaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (duenoId == null) return null;
            return cb.equal(root.get("dueno").get("id"), duenoId);
        };
    }

    private static Specification<ContactoEmergenciaEntity> nombreLike(String nombre) {
        return (Root<ContactoEmergenciaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (nombre == null) return null;
            return cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
        };
    }

    private static Specification<ContactoEmergenciaEntity> telefonoEquals(String telefono) {
        return (Root<ContactoEmergenciaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (telefono == null) return null;
            return cb.equal(root.get("telefono"), telefono);
        };
    }

    private static Specification<ContactoEmergenciaEntity> relacionLike(String relacion) {
        return (Root<ContactoEmergenciaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (relacion == null) return null;
            return cb.like(cb.lower(root.get("relacion")), "%" + relacion.toLowerCase() + "%");
        };
    }
}
