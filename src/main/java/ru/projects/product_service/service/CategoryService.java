package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.exception.CategoryNotFoundException;
import ru.projects.product_service.mapper.CategoryMapper;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toCategory(categoryRequestDto);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponseDto(category);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CategoryTreeResponseDto updateCategory(UUID id, CategoryRequestDto categoryRequestDto) {
        getCategoryOrThrow(id);
        Category category = categoryMapper.toCategory(categoryRequestDto);
        category.setId(id);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryTreeResponseDto(category);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteCategory(UUID id) {
        Category category = getCategoryOrThrow(id);
        categoryRepository.delete(category);
    }

    public List<CategoryTreeResponseDto> getAllCategories() {
        return categoryMapper.toCategoryTreeResponseDtoList(categoryRepository.findByParentIsNull());
    }

    public CategoryResponseDto getCategoryById(UUID id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toCategoryResponseDto(category);
    }

    private Category getCategoryOrThrow(UUID id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + id + " not found")
        );
    }
}
