package ru.projects.product_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.DTO.ProductRequestDto;
import ru.projects.product_service.DTO.ProductResponseDto;
import ru.projects.product_service.DTO.VariationRequestDto;
import ru.projects.product_service.DTO.VariationResponseDto;
import ru.projects.product_service.service.ProductService;

import java.util.Set;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.createProduct(productRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Deleted product with id " + id);
    }


    @PostMapping("/{productId}/variations")
    public ResponseEntity<VariationResponseDto> addVariation(@PathVariable Long productId, @RequestBody @Valid VariationRequestDto variationRequestDto) {
        return ResponseEntity.ok(productService.addVariation(productId, variationRequestDto));
    }

    @GetMapping("/variations")
    public ResponseEntity<Page<VariationResponseDto>> getVariations(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllVariations(pageable));
    }

    @PostMapping("/variationsByIds")
    public ResponseEntity<Set<VariationResponseDto>> getVariationsByIds(@RequestBody Set<Long> ids) {
        return ResponseEntity.ok(productService.getVariationsByIds(ids));
    }

    @PutMapping("/variations/{variationId}")
    public ResponseEntity<VariationResponseDto> updateVariation(@PathVariable Long variationId, @RequestBody @Valid VariationRequestDto variationRequestDto) {
        return ResponseEntity.ok(productService.updateVariation(variationId, variationRequestDto));
    }

    @GetMapping("/{productId}/variations")
    public ResponseEntity<Page<VariationResponseDto>> getVariations(@PathVariable Long productId, Pageable pageable) {
        return ResponseEntity.ok(productService.getVariationsByProductId(productId, pageable));
    }

    @DeleteMapping("/variations/{variationId}")
    public ResponseEntity<String> deleteVariation(@PathVariable Long variationId) {
        productService.deleteVariationById(variationId);
        return ResponseEntity.ok("Deleted variation with id " + variationId);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Page<VariationResponseDto>> getVariationsByCategory(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(productService.getVariationsByCategoryId(id, pageable));
    }
}