package ru.projects.product_service.service;

import ru.projects.product_service.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String name, Long parentId);
    Category updateCategory(Long id, String name, Long parentId);
    void deleteCategory(Long id);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
}
