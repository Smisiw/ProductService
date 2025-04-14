package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.AttributeRepository;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    @Override
    @Transactional
    public Category createCategory(String name, Long parentId, Set<Long> attributeIds) {
        Category category = new Category();
        category.setName(name);
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId).orElseThrow(
                    () -> new RuntimeException("Parent category with id " + parentId + " not found")
            );
            category.setParent(parent);
        }
        if (attributeIds != null) {
            for (Long attributeId : attributeIds) {
                Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(
                        () -> new RuntimeException("Attribute with id " + attributeId + " not found")
                );
                category.getAttributes().add(attribute);
            }
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, String name, Long parentId, Set<Long> attributeIds) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category with id " + id + " not found")
        );
        category.setName(name);
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId).orElseThrow(
                    () -> new RuntimeException("Parent category with id " + parentId + " not found")
            );
            category.setParent(parent);
        } else
            category.setParent(null);
        if (attributeIds != null) {
            Set<Attribute> attributes = new HashSet<>();
            for (Long attributeId : attributeIds) {
                Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(
                        () -> new RuntimeException("Attribute with id " + attributeId + " not found")
                );
                attributes.add(attribute);
            }
            category.setAttributes(attributes);
        }
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
