package com.ecommerce.api.services;

import com.ecommerce.api.dto.products.CreateProductRequest;
import com.ecommerce.api.dto.products.ProductResponse;
import com.ecommerce.api.dto.products.UpdateProductRequest;
import com.ecommerce.api.dto.common.PaginatedResponse;
import com.ecommerce.api.entities.ProductEntity;
import com.ecommerce.api.enums.SystemParamType;
import com.ecommerce.api.exceptions.ExceptionFactory;
import com.ecommerce.api.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repo;
    private final SystemParamService systemParams;

    public ProductService(ProductRepository repo, SystemParamService systemParams) {
        this.repo = repo;
        this.systemParams = systemParams;
    }

    public PaginatedResponse<ProductResponse> searchPublic(String q, int page, int size) {
        int minStock = systemParams.getAsInt(SystemParamType.min_stock_visibility, 1);
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductEntity> res = repo.searchPublic(q, minStock, pr);
        return PaginatedResponse.from(res, this::parseResponse);
    }

    public ProductResponse findOnePublic(UUID id) {
        int minStock = systemParams.getAsInt(SystemParamType.min_stock_visibility, 1);
        var p = repo.findById(id).orElseThrow(() -> ExceptionFactory.productNotFound());
        if (Boolean.FALSE.equals(p.getActive())
                || (p.getStock() != null && p.getStock() < minStock)) {
            throw ExceptionFactory.productNotFound();
        }
        return parseResponse(p);
    }

    public PaginatedResponse<ProductResponse> searchAdmin(String q, int page, int size) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductEntity> res = repo.searchAdmin(q, pr);
        return PaginatedResponse.from(res, this::parseResponse);
    }

    public ProductResponse findOneAdmin(UUID id) {
        var product = repo.findById(id).orElseThrow(() -> ExceptionFactory.productNotFound());
        return parseResponse(product);
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        repo.findBySku(request.getSku()).ifPresent(x -> {
            throw ExceptionFactory.skuAlreadyExists();
        });

        var product = buildProductBody(request);

        var updated = repo.save(product);

        return parseResponse(repo.save(updated));
    }

    @Transactional
    public ProductResponse update(UUID id, UpdateProductRequest request) {
        var product = repo.findById(id).orElseThrow(() -> ExceptionFactory.productNotFound());

        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }

        if (request.getSku() != null) {
            product.setSku(request.getSku());
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

        var updated = repo.save(product);

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
}
