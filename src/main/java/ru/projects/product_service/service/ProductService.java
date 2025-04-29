package ru.projects.product_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.projects.product_service.DTO.ProductRequestDto;
import ru.projects.product_service.DTO.ProductResponseDto;
import ru.projects.product_service.DTO.VariationRequestDto;
import ru.projects.product_service.DTO.VariationResponseDto;


public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);
    Page<ProductResponseDto> getAllProducts(Pageable pageable);
    ProductResponseDto getProductById(Long id);
    void deleteProductById(Long id);
    VariationResponseDto addVariation(Long productId, VariationRequestDto variationRequestDto);
    VariationResponseDto getVariationById(Long variationId);
    Page<VariationResponseDto> getVariationsByProductId(Long productId, Pageable pageable);
    Page<VariationResponseDto> getAllVariations(Pageable pageable);
    void deleteVariationById(Long variationId);
    VariationResponseDto updateVariation(Long variationId, VariationRequestDto variationRequestDto);
}