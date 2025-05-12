package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.exception.CategoryNotFoundException;
import ru.projects.product_service.mapper.CategoryMapper;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Secured("ROLE_ADMIN")
    public CategoryTreeResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toCategory(categoryRequestDto);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryTreeResponseDto(category);
    }

    @Transactional
    @Secured("ROLE_ADMIN")
    public CategoryTreeResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        getCategoryOrThrow(id);
        Category category = categoryMapper.toCategory(categoryRequestDto);
        category.setId(id);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryTreeResponseDto(category);
    }

    @Transactional
    @Secured("ROLE_ADMIN")
    public void deleteCategory(Long id) {
        Category category = getCategoryOrThrow(id);
        categoryRepository.delete(category);
    }

    public List<CategoryTreeResponseDto> getAllCategories() {
        return categoryMapper.toCategoryTreeResponseDtoList(categoryRepository.findByParentIsNull());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toCategoryResponseDto(category);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + id + " not found")
        );
    }
}
