package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.DTO.CategoryRequest;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request.getName(), request.getParentId(), request.getAttributeIds());
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        Category category = categoryService.updateCategory(id, request.getName(), request.getParentId(), request.getAttributeIds());
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category " + id + " was successfully deleted");
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
