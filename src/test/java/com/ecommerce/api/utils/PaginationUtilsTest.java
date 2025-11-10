package com.ecommerce.api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaginationUtils Tests")
class PaginationUtilsTest {

    @Test
    @DisplayName("Should create pageable successfully")
    void testToPageable_Success() {
        // Act
        Pageable result = PaginationUtils.toPageable(1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPageNumber()); // Page is 0-indexed
        assertEquals(10, result.getPageSize());
    }

    @Test
    @DisplayName("Should handle page less than 1")
    void testToPageable_PageLessThanOne() {
        // Act
        Pageable result = PaginationUtils.toPageable(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPageNumber()); // Should be clamped to 0
    }

    @Test
    @DisplayName("Should handle size less than 1")
    void testToPageable_SizeLessThanOne() {
        // Act
        Pageable result = PaginationUtils.toPageable(1, 0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPageSize()); // Should be clamped to 1
    }

    @Test
    @DisplayName("Should create pageable with sort successfully")
    void testToPageable_WithSort() {
        // Act
        Pageable result = PaginationUtils.toPageable(1, 10, "title", Sort.Direction.ASC);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertTrue(result.getSort().isSorted());
    }

    @Test
    @DisplayName("Should validate pagination params successfully")
    void testValidatePaginationParams_Success() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> PaginationUtils.validatePaginationParams(1, 10));
    }

    @Test
    @DisplayName("Should throw exception for page less than 1")
    void testValidatePaginationParams_InvalidPage() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> PaginationUtils.validatePaginationParams(0, 10));
    }

    @Test
    @DisplayName("Should throw exception for size less than 1")
    void testValidatePaginationParams_InvalidSize() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> PaginationUtils.validatePaginationParams(1, 0));
    }

    @Test
    @DisplayName("Should throw exception for size greater than 100")
    void testValidatePaginationParams_SizeTooLarge() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> PaginationUtils.validatePaginationParams(1, 101));
    }

    @Test
    @DisplayName("Should calculate offset correctly")
    void testCalculateOffset_Success() {
        // Act
        int result = PaginationUtils.calculateOffset(2, 10);

        // Assert
        assertEquals(10, result);
    }

    @Test
    @DisplayName("Should calculate offset for first page")
    void testCalculateOffset_FirstPage() {
        // Act
        int result = PaginationUtils.calculateOffset(1, 10);

        // Assert
        assertEquals(0, result);
    }
}

