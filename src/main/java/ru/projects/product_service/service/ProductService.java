package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.*;
import ru.projects.product_service.exception.ProductNotFoundException;
import ru.projects.product_service.exception.RolePermissionExceprion;
import ru.projects.product_service.mapper.ProductMapper;
import ru.projects.product_service.mapper.VariationMapper;
import ru.projects.product_service.model.*;
import ru.projects.product_service.repository.*;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductMapper productMapper;
    private final VariationMapper variationMapper;

    @Transactional
    @Secured(value = {"ROLE_ADMIN, ROLE_SELLER"})
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Product product = productMapper.toProduct(productRequestDto, Long.parseLong(userDetails.getUsername()));
        return productMapper.toProductResponseDto(productRepository.save(product));
    }

    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toProductResponseDto);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = getProductOrThrow(id);
        return productMapper.toProductResponseDto(product);
    }

    @Transactional
    @Secured(value = {"ROLE_ADMIN, ROLE_SELLER"})
    public void deleteProductById(Long id) {
        Product product = getProductOrThrow(id);
        validatePermission(id);
        productRepository.delete(product);
    }

    @Transactional
    @Secured(value = {"ROLE_ADMIN, ROLE_SELLER"})
    public VariationResponseDto addVariation(Long productId, VariationRequestDto variationRequestDto) {
        Product product = getProductOrThrow(productId);
        validatePermission(productId);
        ProductVariation variation = variationMapper.toProductVariation(variationRequestDto);
        product.addVariation(variation);
        productRepository.save(product);
        return variationMapper.toVariationResponseDto(variation);
    }

    public VariationResponseDto getVariationById(Long variationId) {
        ProductVariation variation = productVariationRepository.findById(variationId).orElseThrow(() -> new ProductNotFoundException("Product variation with id " + variationId + " not found"));
        return variationMapper.toVariationResponseDto(variation);
    }

    public Page<VariationResponseDto> getVariationsByProductId(Long productId, Pageable pageable) {
        return productVariationRepository.findByProductId(productId, pageable).map(variationMapper::toVariationResponseDto);
    }

    public Page<VariationResponseDto> getAllVariations(Pageable pageable) {
        return productVariationRepository.findAll(pageable).map(variationMapper::toVariationResponseDto);
    }

    public Page<VariationResponseDto> getVariationsByCategoryId(Long categoryId, Pageable pageable) {
        return productVariationRepository.findByProductCategoryId(categoryId, pageable).map(variationMapper::toVariationResponseDto);
    }


    @Transactional
    @Secured(value = {"ROLE_ADMIN, ROLE_SELLER"})
    public void deleteVariationById(Long variationId) {
        productVariationRepository.deleteById(variationId);
    }

    @Transactional
    @Secured(value = {"ROLE_ADMIN, ROLE_SELLER"})
    public VariationResponseDto updateVariation(Long variationId, VariationRequestDto variationRequestDto) {
        productVariationRepository.findById(variationId).orElseThrow(
                () -> new ProductNotFoundException("Product variation with id " + variationId + " not found")
        );
        ProductVariation variation = variationMapper.toProductVariation(variationRequestDto);
        variation.setId(variationId);
        productVariationRepository.save(variation);
        return variationMapper.toVariationResponseDto(variation);
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    private void validatePermission(Long sellerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Long userId = Long.parseLong(userDetails.getUsername());

        if (!isAdmin && !userId.equals(sellerId)) {
            throw new RolePermissionExceprion("User has no permission to this product");
        }
    }
}
