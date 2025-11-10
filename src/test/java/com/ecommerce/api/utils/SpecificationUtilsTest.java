package com.ecommerce.api.utils;

import com.ecommerce.api.entities.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SpecificationUtils Tests")
class SpecificationUtilsTest {


    @Test
    @DisplayName("Should return specification when value is not null")
    void testOptional_NonNullValue() {
        // Act
        Specification<ProductEntity> result = SpecificationUtils.optional("test",
                value -> ProductSpecifications.nameContains(value));

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should combine specifications with AND")
    void testCombineAnd_Success() {
        // Arrange
        Specification<ProductEntity> spec1 = ProductSpecifications.nameContains("test");
        Specification<ProductEntity> spec2 = ProductSpecifications.skuContains("SKU");

        // Act
        Specification<ProductEntity> result = SpecificationUtils.combineAnd(spec1, spec2);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should combine specifications with OR")
    void testCombineOr_Success() {
        // Arrange
        Specification<ProductEntity> spec1 = ProductSpecifications.nameContains("test");
        Specification<ProductEntity> spec2 = ProductSpecifications.skuContains("SKU");

        // Act
        Specification<ProductEntity> result = SpecificationUtils.combineOr(spec1, spec2);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should handle null specifications in combineAnd")
    void testCombineAnd_WithNulls() {
        // Arrange
        Specification<ProductEntity> spec1 = ProductSpecifications.nameContains("test");
        Specification<ProductEntity> spec2 = null;

        // Act
        Specification<ProductEntity> result = SpecificationUtils.combineAnd(spec1, spec2);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return null for empty search term")
    void testSearchInMultipleFields_EmptyTerm() {
        // Act
        Specification<ProductEntity> result = SpecificationUtils.searchInMultipleFields("",
                term -> ProductSpecifications.nameContains(term));

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null for null search term")
    void testSearchInMultipleFields_NullTerm() {
        // Act
        Specification<ProductEntity> result = SpecificationUtils.searchInMultipleFields(null,
                term -> ProductSpecifications.nameContains(term));

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should create search specification for valid term")
    void testSearchInMultipleFields_ValidTerm() {
        // Act
        Specification<ProductEntity> result = SpecificationUtils.searchInMultipleFields("test",
                term -> ProductSpecifications.nameContains(term));

        // Assert
        assertNotNull(result);
    }
}

