package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.ProductVariationRequest;
import ru.projects.product_service.exception.ProductNotFoundException;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.model.AttributeValue;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.AttributeRepository;
import ru.projects.product_service.repository.AttributeValueRepository;
import ru.projects.product_service.repository.ProductRepository;
import ru.projects.product_service.repository.ProductVariationRepository;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductVariation addVariation(Long productId, ProductVariationRequest request) {
        Product product;
        if (productId == null) {
            product = new Product();
        } else {
            product = productRepository.findById(productId).orElseThrow(
                    () -> new ProductNotFoundException("Product with id " + productId + " not found")
            );
        }
        ProductVariation variation = new ProductVariation();
        variation.setName(request.getName());
        variation.setDescription(request.getDescription());
        variation.setPrice(request.getPrice());
        variation.setQuantity(request.getQuantity());
        Set<AttributeValue> attributeValues = new HashSet<>();
        request.getAttributeValues().forEach((key, value) -> {
            Attribute attribute = attributeRepository.findById(key).orElseThrow(
                    () -> new RuntimeException("Attribute with id " + key + " not found")
            );
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setAttribute(attribute);
            attributeValue.setValue(value);
            attributeValues.add(attributeValue);
        });
        variation.setAttributeValues(attributeValues);
        product.addVariation(variation);
        return productVariationRepository.save(variation);
    }

    @Override
    public ProductVariation getVariationById(Long variationId) {
        return productVariationRepository.findById(variationId).orElseThrow(() -> new ProductNotFoundException("Product variation with id " + variationId + " not found"));
    }

    @Override
    public Page<ProductVariation> getVariationsByProductId(Long productId, Pageable pageable) {
        return productVariationRepository.findByProductId(productId, pageable);
    }

    @Override
    public Page<ProductVariation> getAllVariations(Pageable pageable) {
        return productVariationRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public void deleteVariationById(Long variationId) {
        productVariationRepository.deleteById(variationId);
    }

    @Override
    @Transactional
    public ProductVariation updateVariation(Long variationId, ProductVariationRequest request) {
        ProductVariation variation = getVariationById(variationId);
        variation.setName(request.getName());
        variation.setDescription(request.getDescription());
        variation.setPrice(request.getPrice());
        variation.setQuantity(request.getQuantity());
        Set<AttributeValue> attributeValues = new HashSet<>();
        request.getAttributeValues().forEach((key, value) -> {
            Attribute attribute = attributeRepository.findById(key).orElseThrow(
                    () -> new RuntimeException("Attribute with id " + key + " not found")
            );
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setAttribute(attribute);
            attributeValue.setValue(value);
            attributeValues.add(attributeValue);
        });
        variation.setAttributeValues(attributeValues);
        return productVariationRepository.save(variation);
    }

}
