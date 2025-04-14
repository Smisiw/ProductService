package ru.projects.product_service.service;

import ru.projects.product_service.model.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    Category createCategory(String name, Long parentId, Set<Long> attributeIds);
    Category updateCategory(Long id, String name, Long parentId, Set<Long> attributeIds);
    void deleteCategory(Long id);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
}
