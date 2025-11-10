package com.ecommerce.api.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public class SpecificationUtils {

    public static <T, E> Specification<E> optional(T value, Function<T, Specification<E>> specFn) {
        return value == null ? null : specFn.apply(value);
    }

    @SafeVarargs
    public static <E> Specification<E> combineAnd(Specification<E>... specs) {
        Specification<E> result = Specification.where(null);
        for (Specification<E> spec : specs) {
            if (spec != null) {
                result = result.and(spec);
            }
        }
        return result;
    }
`
    @SafeVarargs
    public static <E> Specification<E> combineOr(Specification<E>... specs) {
        Specification<E> result = Specification.where(null);
        for (Specification<E> spec : specs) {
            if (spec != null) {
                result = result.or(spec);
            }
        }
        return result;
    }

    public static <E> Specification<E> searchInMultipleFields(String searchTerm,
            Function<String, Specification<E>>... fieldSpecs) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return null;
        }

        String term = searchTerm.toLowerCase().trim();
        return combineOr(fieldSpecs[0].apply(term),
                fieldSpecs.length > 1 ? fieldSpecs[1].apply(term) : null,
                fieldSpecs.length > 2 ? fieldSpecs[2].apply(term) : null);
    }
}
