package com.ecommerce.api.services;

import com.ecommerce.api.dto.products.CreateProductRequest;
import com.ecommerce.api.dto.products.ProductQuery;
import com.ecommerce.api.dto.products.ProductResponse;
import com.ecommerce.api.dto.products.UpdateProductRequest;
import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.entities.ProductEntity;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.ProductRepository;
import com.ecommerce.api.utils.PaginationUtils;
import com.ecommerce.api.utils.ProductSpecifications;
import com.ecommerce.api.utils.SpecificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final SystemParamService systemParamsService;

    public ProductService(ProductRepository productRepository,
            SystemParamService systemParamsService) {
        this.productRepository = productRepository;
        this.systemParamsService = systemParamsService;
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ProductResponse> searchPublic(ProductQuery query) {
        log.debug("Searching public products with filters: {}", query);

        PaginationUtils.validatePaginationParams(query.getPage(), query.getSize());

        Specification<ProductEntity> specification = buildPublicSpecification(query);

        Pageable pageable = PaginationUtils.toPageable(query.getPage(), query.getSize());

        Page<ProductEntity> page = productRepository.findAll(specification, pageable);

        return PaginatedResponse.from(page, this::parseResponse);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ProductResponse> searchAdmin(ProductQuery query) {
        log.debug("Searching admin products with filters: {}", query);

        PaginationUtils.validatePaginationParams(query.getPage(), query.getSize());

        // Para búsqueda admin: puede incluir activos e inactivos
        Specification<ProductEntity> specification = buildAdminSpecification(query);

        Pageable pageable = PaginationUtils.toPageable(query.getPage(), query.getSize());

        Page<ProductEntity> page = productRepository.findAll(specification, pageable);

        return PaginatedResponse.from(page, this::parseResponse);
    }

    private Specification<ProductEntity> buildPublicSpecification(ProductQuery query) {
        return Specification.where(ProductSpecifications.isActive()).and(buildCommonFilters(query));
    }

    private Specification<ProductEntity> buildAdminSpecification(ProductQuery query) {
        return buildCommonFilters(query);
    }

    private Specification<ProductEntity> buildCommonFilters(ProductQuery query) {
        Specification<ProductEntity> specification = Specification.where(null);

        specification = specification
                .and(SpecificationUtils.optional(query.getName(),
                        ProductSpecifications::nameContains))
                .and(SpecificationUtils.optional(query.getSku(),
                        ProductSpecifications::skuContains))
                .and(SpecificationUtils.optional(query.getHasStock(),
                        ProductSpecifications::hasStock))
                .and(SpecificationUtils.optional(query.getMinPrice(),
                        ProductSpecifications::priceAtLeast))
                .and(SpecificationUtils.optional(query.getMaxPrice(),
                        ProductSpecifications::priceAtMost));

        return specification;
    }

    public ProductResponse findOnePublic(UUID id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> ExceptionFactory.productNotFound());

        validateProductVisibility(product);

        return parseResponse(product);
    }

    public ProductResponse findOneAdmin(UUID id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> ExceptionFactory.productNotFound());
        return parseResponse(product);
    }


    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        productRepository.findBySku(request.getSku()).ifPresent(x -> {
            throw ExceptionFactory.skuAlreadyExists();
        });

        var product = buildProductBody(request);
        var saved = productRepository.save(product);

        return parseResponse(saved);
    }

    @Transactional
    public ProductResponse update(UUID id, UpdateProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> ExceptionFactory.productNotFound());

        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPriceCents() != null) {
            product.setPriceCents(request.getPriceCents());
        }
        if (request.getPhotoUrl() != null) {
            product.setPhotoUrl(request.getPhotoUrl());
        }
        if (request.getTax() != null) {
            product.setTax(request.getTax());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }

        var updated = productRepository.save(product);
        return parseResponse(updated);
    }

    private ProductResponse parseResponse(ProductEntity product) {
        var productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setTitle(product.getTitle());
        productResponse.setSku(product.getSku());
        productResponse.setDescription(product.getDescription());
        productResponse.setPriceCents(product.getPriceCents());
        productResponse.setPhotoUrl(product.getPhotoUrl());
        productResponse.setTax(product.getTax());
        productResponse.setActive(product.getActive());
        productResponse.setStock(product.getStock());
        return productResponse;
    }

    private ProductEntity buildProductBody(CreateProductRequest request) {
        var product = new ProductEntity();
        product.setTitle(request.getTitle());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setPriceCents(request.getPriceCents());
        product.setPhotoUrl(request.getPhotoUrl());
        product.setTax(request.getTax());
        product.setActive(true);
        product.setStock(request.getStock());
        return product;
    }

    private void validateProductVisibility(ProductEntity product) {
        int minStock = systemParamsService.getAsInt(SystemParamType.min_stock_visibility, 15);

        if (Boolean.FALSE.equals(product.getActive())
                || (product.getStock() != null && product.getStock() < minStock)) {
            throw ExceptionFactory.productNotFound();
        }
    }

}
