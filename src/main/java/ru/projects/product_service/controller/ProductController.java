package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.service.ProductService;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Deleted product with id " + id);
    }


    @PostMapping("/{productId}/variations")
    public ResponseEntity<ProductVariation> addVariation(@PathVariable Long productId, @RequestBody ProductVariation variation) {
        return ResponseEntity.ok(productService.addVariation(productId, variation));
    }

    @GetMapping("/variations")
    public ResponseEntity<Page<ProductVariation>> getVariations(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllVariations(pageable));
    }

    @PutMapping("/variations/{variationId}")
    public ResponseEntity<ProductVariation> updateVariation(@PathVariable Long variationId, @RequestBody ProductVariation variation) {
        return ResponseEntity.ok(productService.updateVariation(variationId, variation));
    }

    @GetMapping("/{productId}/variations")
    public ResponseEntity<Page<ProductVariation>> getVariations(@PathVariable Long productId, Pageable pageable) {
        return ResponseEntity.ok(productService.getVariationsByProductId(productId, pageable));
    }

    @DeleteMapping("/variations/{variationId}")
    public ResponseEntity<?> deleteVariation(@PathVariable Long variationId) {
        productService.deleteVariationById(variationId);
        return ResponseEntity.ok("Deleted variation with id " + variationId);
    }
}