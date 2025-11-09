package com.ecommerce.api.dto;

import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

public class PaginatedResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public static <S, T> PaginatedResponse<T> from(Page<S> pageData, Function<S, T> mapper) {
        PaginatedResponse<T> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.items = pageData.getContent().stream().map(mapper).toList();
        paginatedResponse.page = pageData.getNumber();
        paginatedResponse.size = pageData.getSize();
        paginatedResponse.totalItems = pageData.getTotalElements();
        paginatedResponse.totalPages = pageData.getTotalPages();
        paginatedResponse.hasNext = pageData.hasNext();
        paginatedResponse.hasPrev = pageData.hasPrevious();
        return paginatedResponse;

    }
}
