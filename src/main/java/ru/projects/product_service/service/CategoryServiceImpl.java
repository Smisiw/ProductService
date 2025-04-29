package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.mapper.CategoryMapper;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryTreeResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toCategory(categoryRequestDto);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryTreeResponseDto(category);
    }

    @Override
    @Transactional
    public CategoryTreeResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category with id " + id + " not found")
        );
        Category category = categoryMapper.toCategory(categoryRequestDto);
        category.setId(id);
        category = categoryRepository.save(category);
        return categoryMapper.toCategoryTreeResponseDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryTreeResponseDto> getAllCategories() {
        return categoryMapper.toCategoryTreeResponseDtoList(categoryRepository.findByParentIsNull());
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
        return categoryMapper.toCategoryResponseDto(category);
    }
}
