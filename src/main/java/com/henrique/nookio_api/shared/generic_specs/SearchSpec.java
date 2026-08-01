package com.henrique.nookio_api.shared.generic_specs;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SearchSpec<T> implements Specification<T> {

    private final String searchTerm;
    private final List<String> fields;

    public SearchSpec(String searchTerm, List<String> fields) {
        this.searchTerm = searchTerm;
        this.fields = fields;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (searchTerm == null || searchTerm.isBlank() || fields == null || fields.isEmpty()) {
            return builder.conjunction();
        }

        String pattern = "%" + searchTerm.trim().toLowerCase() + "%";
        List<Predicate> predicates = new ArrayList<>();

        for (String fieldName : fields) {
            Expression<String> expression = getExpression(root, fieldName);
            predicates.add(builder.like(builder.lower(expression), pattern));
        }

        return builder.or(predicates.toArray(new Predicate[0]));
    }

    private Expression<String> getExpression(Root<T> root, String fieldName) {
        if (fieldName.contains(".")) {
            String[] parts = fieldName.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path.as(String.class);
        }
        return root.get(fieldName).as(String.class);
    }
}
