package ru.projects.product_service.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.DTO.*;
import ru.projects.product_service.service.ProductService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.createProduct(productRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Deleted product with id " + id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{productId}/variations")
    public ResponseEntity<VariationResponseDto> addVariation(@PathVariable UUID productId, @RequestBody @Valid VariationRequestDto variationRequestDto) {
        return ResponseEntity.ok(productService.addVariation(productId, variationRequestDto));
    }

    @GetMapping("/variations")
    public ResponseEntity<Page<VariationResponseDto>> getVariations(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllVariations(pageable));
    }

    @PostMapping("/variationsByIds")
    public ResponseEntity<List<VariationResponseDto>> getVariationsByIds(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(productService.getVariationsByIds(ids));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/variations/{variationId}")
    public ResponseEntity<VariationResponseDto> updateVariation(@PathVariable UUID variationId, @RequestBody @Valid VariationRequestDto variationRequestDto) {
        return ResponseEntity.ok(productService.updateVariation(variationId, variationRequestDto));
    }

    @GetMapping("/{productId}/variations")
    public ResponseEntity<Page<VariationResponseDto>> getVariations(@PathVariable UUID productId, Pageable pageable) {
        return ResponseEntity.ok(productService.getVariationsByProductId(productId, pageable));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/variations/{variationId}")
    public ResponseEntity<String> deleteVariation(@PathVariable UUID variationId) {
        productService.deleteVariationById(variationId);
        return ResponseEntity.ok("Deleted variation with id " + variationId);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Page<VariationResponseDto>> getVariationsByCategory(@PathVariable UUID id, Pageable pageable) {
        return ResponseEntity.ok(productService.getVariationsByCategoryId(id, pageable));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/checkAndReserve")
    public ResponseEntity<String> checkAndReserve(@RequestBody List<CheckAndReserveItemRequestDto> checkAndReserveItemRequestDtos) {
        productService.checkAndReserve(checkAndReserveItemRequestDtos);
        return ResponseEntity.ok("Products reserved successfully");
    }
}