package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryTreeResponseDto> create(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryTreeResponseDto category = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryTreeResponseDto> update(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryTreeResponseDto category = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category " + id + " was successfully deleted");
    }

    @GetMapping
    public ResponseEntity<List<CategoryTreeResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
