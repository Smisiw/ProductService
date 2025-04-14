package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.projects.product_service.exception.ProductNotFoundException;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.ProductRepository;
import ru.projects.product_service.repository.ProductVariationRepository;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;

    @Override
    @Transactional
    //@CacheEvict(value = "productList")
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    //@Cacheable(value = "productList", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    //@Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    //@CacheEvict(value = "productList", allEntries = true)
    //@CacheEvict(value = "products", key = "#id")
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "productVariationList")
    public ProductVariation addVariation(Long productId, ProductVariation variation) {
        Product product = getProductById(productId);
        variation.setProduct(product);
        return productVariationRepository.save(variation);
    }

    @Override
    //@Cacheable(value = "productVariations", key = "#variationId")
    public ProductVariation getVariationById(Long variationId) {
        return productVariationRepository.findById(variationId).orElseThrow(() -> new ProductNotFoundException("Product variation with id " + variationId + " not found"));
    }

    @Override
    //@Cacheable(value = "productVariationList", key = "#productId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductVariation> getVariationsByProductId(Long productId, Pageable pageable) {
        return productVariationRepository.findByProductId(productId, pageable);
    }

    @Override
    //@Cacheable(value = "productVariationList", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductVariation> getAllVariations(Pageable pageable) {
        return productVariationRepository.findAll(pageable);
    }


    @Override
    @Transactional
    //@CacheEvict(value = "productVariationList", allEntries = true)
    //@CacheEvict(value = "productVariations", key = "#variationId")
    public void deleteVariationById(Long variationId) {
        productVariationRepository.deleteById(variationId);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "productVariationList", allEntries = true)
    //@CacheEvict(value = "productVariations", key = "#variationId")
    public ProductVariation updateVariation(Long variationId, ProductVariation updatedVariation) {
        ProductVariation variation = getVariationById(variationId);
        variation.setName(updatedVariation.getName());
        variation.setDescription(updatedVariation.getDescription());
        variation.setPrice(updatedVariation.getPrice());
        variation.setQuantity(updatedVariation.getQuantity());
        variation.setAttributes(updatedVariation.getAttributes());
        return productVariationRepository.save(variation);
    }
}
