package com.example.borrowit.Specification;

import com.example.borrowit.Entity.Reclamation;
import com.example.borrowit.DTO.ReclamationSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate; // Updated import
import java.util.ArrayList;
import java.util.List;

public class ReclamationSpecification {

    public static Specification<Reclamation> search(ReclamationSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getId() != null) {
                predicates.add(builder.equal(root.get("id"), criteria.getId()));
            }

            if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
                predicates.add(builder.like(root.get("status"), "%" + criteria.getStatus() + "%"));
            }

            if (criteria.getStartDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("date"), criteria.getStartDate()));
            }

            if (criteria.getEndDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("date"), criteria.getEndDate()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
