package com.ecommerce.api.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    public static Pageable toPageable(int page, int size) {
        return PageRequest.of(Math.max(page - 1, 0), Math.max(size, 1));
    }

    public static Pageable toPageable(int page, int size, String sortBy, Sort.Direction direction) {
        Sort sort = Sort.by(direction, sortBy);
        return PageRequest.of(Math.max(page - 1, 0), Math.max(size, 1), sort);
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than 0");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }
    }

    public static int calculateOffset(int page, int size) {
        return (page - 1) * size;
    }
}
