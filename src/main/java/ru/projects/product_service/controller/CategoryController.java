package ru.projects.product_service.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto category = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.ok(category);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryTreeResponseDto> update(@PathVariable UUID id, @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        CategoryTreeResponseDto category = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(category);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category " + id + " was successfully deleted");
    }

    @GetMapping
    public ResponseEntity<List<CategoryTreeResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
