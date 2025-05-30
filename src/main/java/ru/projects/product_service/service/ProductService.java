package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.*;
import ru.projects.product_service.exception.NotRelevantProductInfoException;
import ru.projects.product_service.exception.ProductNotFoundException;
import ru.projects.product_service.exception.ProductReservationException;
import ru.projects.product_service.exception.RolePermissionExceprion;
import ru.projects.product_service.mapper.ProductMapper;
import ru.projects.product_service.mapper.VariationMapper;
import ru.projects.product_service.model.*;
import ru.projects.product_service.repository.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ProductMapper productMapper;
    private final VariationMapper variationMapper;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        Product product = productMapper.toProduct(productRequestDto, userId);
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public void deleteProductById(Long id) {
        Product product = getProductOrThrow(id);
        validatePermission(id);
        productRepository.delete(product);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public VariationResponseDto addVariation(Long productId, VariationRequestDto variationRequestDto) {
        Product product = getProductOrThrow(productId);
        validatePermission(productId);
        ProductVariation variation = variationMapper.toProductVariation(variationRequestDto);
        product.addVariation(variation);
        productRepository.save(product);
        return variationMapper.toVariationResponseDto(variation);
    }

    public Set<VariationResponseDto> getVariationsByIds(Set<Long> variationIds) {
        Set<ProductVariation> variations = new HashSet<>(productVariationRepository.findAllById(variationIds));
        return variationMapper.toVariationResponseDtoSet(variations);
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public void deleteVariationById(Long variationId) {
        productVariationRepository.deleteById(variationId);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public VariationResponseDto updateVariation(Long variationId, VariationRequestDto variationRequestDto) {
        productVariationRepository.findById(variationId).orElseThrow(
                () -> new ProductNotFoundException("Product variation with id " + variationId + " not found")
        );
        ProductVariation variation = variationMapper.toProductVariation(variationRequestDto);
        variation.setId(variationId);
        productVariationRepository.save(variation);
        return variationMapper.toVariationResponseDto(variation);
    }

    @Transactional
    public void checkAndReserve(Set<CheckAndReserveItemRequestDto> checkAndReserveItemRequestDtos) {
        Set<Long> productVariationsIds = checkAndReserveItemRequestDtos.stream()
                .map(CheckAndReserveItemRequestDto::productVariationId)
                .collect(Collectors.toSet());
        Map<Long, CheckAndReserveItemRequestDto> requestDtoMap = checkAndReserveItemRequestDtos.stream()
                .collect(Collectors.toMap(CheckAndReserveItemRequestDto::productVariationId, Function.identity()));
        Set<ProductVariation> productVariations = new HashSet<>(productVariationRepository.findAllById(productVariationsIds));
        if (productVariations.size() != checkAndReserveItemRequestDtos.size()) {
            throw new NotRelevantProductInfoException("Some products are unavailable");
        }
        productVariations.forEach(productVariation -> {
            CheckAndReserveItemRequestDto requestDto = requestDtoMap.get(productVariation.getId());
            if (requestDto.unitPrice().compareTo(productVariation.getPrice()) != 0) {
                throw new NotRelevantProductInfoException("The price of some products has changed");
            }
            if (requestDto.quantity() > productVariation.getQuantity() - productVariation.getReserved()) {
                throw new NotRelevantProductInfoException("Some products are unavailable");
            }
            productVariation.setReserved(productVariation.getReserved() + requestDto.quantity());
        });
        productVariationRepository.saveAll(productVariations);
    }

    @Transactional
    public void cancelReservation(OrderItemCancelledEvent orderItemCancelledEvent) {
        ProductVariation variation = productVariationRepository.findById(orderItemCancelledEvent.productVariationId()).orElseThrow(
                () -> new ProductNotFoundException("Product variation with id " + orderItemCancelledEvent.productVariationId() + " not found")
        );
        if (variation.getReserved() < orderItemCancelledEvent.quantity()) {
            throw new ProductReservationException("Reserved products count less than the necessary");
        }
        variation.setReserved(variation.getReserved() - orderItemCancelledEvent.quantity());
        productVariationRepository.save(variation);
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    private void validatePermission(Long sellerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userId.equals(sellerId)) {
            throw new RolePermissionExceprion("User has no permission to this product");
        }
    }
}
