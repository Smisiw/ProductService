package ru.projects.product_service.service;

import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryTreeResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    CategoryTreeResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);
    void deleteCategory(Long id);
    List<CategoryTreeResponseDto> getAllCategories();
    CategoryResponseDto getCategoryById(Long id);
}
