package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.*;
import ru.projects.product_service.exception.ProductNotFoundException;
import ru.projects.product_service.mapper.ProductMapper;
import ru.projects.product_service.mapper.VariationMapper;
import ru.projects.product_service.model.*;
import ru.projects.product_service.repository.*;



@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductMapper productMapper;
    private final VariationMapper variationMapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);
        return productMapper.toProductResponseDto(productRepository.save(product));
    }

    @Override
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toProductResponseDto);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + id + " not found")
        );
        return productMapper.toProductResponseDto(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public VariationResponseDto addVariation(Long productId, VariationRequestDto variationRequestDto) {
        Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ProductNotFoundException("Product with id " + productId + " not found")
            );
        ProductVariation variation = variationMapper.toProductVariation(variationRequestDto);
        product.addVariation(variation);
        productRepository.save(product);
        return variationMapper.toVariationResponseDto(variation);
    }

    @Override
    public VariationResponseDto getVariationById(Long variationId) {
        ProductVariation variation = productVariationRepository.findById(variationId).orElseThrow(() -> new ProductNotFoundException("Product variation with id " + variationId + " not found"));
        return variationMapper.toVariationResponseDto(variation);
    }

    @Override
    public Page<VariationResponseDto> getVariationsByProductId(Long productId, Pageable pageable) {
        return productVariationRepository.findByProductId(productId, pageable).map(variationMapper::toVariationResponseDto);
    }

    @Override
    public Page<VariationResponseDto> getAllVariations(Pageable pageable) {
        return productVariationRepository.findAll(pageable).map(variationMapper::toVariationResponseDto);
    }


    @Override
    @Transactional
    public void deleteVariationById(Long variationId) {
        productVariationRepository.deleteById(variationId);
    }

    @Override
    @Transactional
    public VariationResponseDto updateVariation(Long variationId, VariationRequestDto variationRequestDto) {
        productVariationRepository.findById(variationId).orElseThrow(
                () -> new ProductNotFoundException("Product variation with id " + variationId + " not found")
        );
        ProductVariation variation = variationMapper.toProductVariation(variationRequestDto);
        variation.setId(variationId);
        productVariationRepository.save(variation);
        return variationMapper.toVariationResponseDto(variation);
    }

}
