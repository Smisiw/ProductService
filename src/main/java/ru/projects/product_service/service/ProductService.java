package ru.projects.product_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.projects.product_service.DTO.ProductVariationRequest;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;


public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    ProductVariation addVariation(Long productId, ProductVariationRequest request);
    ProductVariation getVariationById(Long variationId);
    Page<ProductVariation> getVariationsByProductId(Long productId, Pageable pageable);
    Page<ProductVariation> getAllVariations(Pageable pageable);
    void deleteVariationById(Long variationId);
    ProductVariation updateVariation(Long variationId, ProductVariationRequest request);
}