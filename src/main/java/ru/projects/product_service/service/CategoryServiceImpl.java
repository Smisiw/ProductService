package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(String name, Long parentId) {
        Category category = new Category();
        category.setName(name);
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId).orElseThrow(
                    () -> new RuntimeException("Parent category not found")
            );
            category.setParent(parent);
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, String name, Long parentId) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
        category.setName(name);
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId).orElseThrow(
                    () -> new RuntimeException("Parent category not found")
            );
            category.setParent(parent);
        } else
            category.setParent(null);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findByParentIsNull();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
    }
}
