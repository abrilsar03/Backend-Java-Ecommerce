package com.ecommerce.api.dto.common;

import org.springframework.data.domain.Page;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedResponse<T> {
    private List<T> items;
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    public PaginatedResponse(List<T> items, int currentPage, int pageSize, long totalItems,
            int totalPages, boolean hasNext, boolean hasPrevious, boolean isFirst, boolean isLast) {
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }


    public static <S, T> PaginatedResponse<T> from(Page<S> pageData, Function<S, T> mapper) {
        List<T> items = pageData.getContent().stream().map(mapper).collect(Collectors.toList());

        return new PaginatedResponse<>(items, pageData.getNumber() + 1, // Convertir a 1-based index
                pageData.getSize(), pageData.getTotalElements(), pageData.getTotalPages(),
                pageData.hasNext(), pageData.hasPrevious(), pageData.isFirst(), pageData.isLast());
    }


    public static <T> PaginatedResponse<T> from(Page<T> pageData) {
        return from(pageData, Function.identity());
    }

    public static <T> PaginatedResponse<T> of(List<T> items, int currentPage, int pageSize,
            long totalItems, int totalPages) {
        boolean hasPrevious = currentPage > 1;
        boolean hasNext = currentPage < totalPages;
        boolean isFirst = currentPage == 1;
        boolean isLast = currentPage == totalPages || totalPages == 0;

        return new PaginatedResponse<>(items, currentPage, pageSize, totalItems, totalPages,
                hasNext, hasPrevious, isFirst, isLast);
    }
}
