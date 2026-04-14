package ru.projects.product_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.exception.CategoryNotFoundException;
import ru.projects.product_service.mapper.CategoryMapper;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private final UUID categoryId = UUID.randomUUID();

    @Test
    void createCategory_savesAndReturnsDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "electronics", "Электроника", null);
        Category category = new Category("Электроника", "electronics");
        category.setId(categoryId);
        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "electronics", "Электроника");

        when(categoryMapper.toCategory(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.createCategory(requestDto);

        assertEquals("Электроника", result.name());
        assertEquals("electronics", result.routeLocation());
        verify(categoryRepository).save(category);
    }

    @Test
    void getCategoryById_returnsDto_whenFound() {
        Category category = new Category("Смартфоны", "smartphones");
        category.setId(categoryId);
        CategoryResponseDto responseDto = new CategoryResponseDto(categoryId, "smartphones", "Смартфоны");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getCategoryById(categoryId);

        assertEquals(categoryId, result.id());
        assertEquals("Смартфоны", result.name());
    }

    @Test
    void getCategoryById_throws_whenNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
    }

    @Test
    void updateCategory_savesUpdatedCategoryAndReturnsTree() {
        CategoryRequestDto requestDto = new CategoryRequestDto(null, "laptops", "Ноутбуки", null);
        Category updated = new Category("Ноутбуки", "laptops");
        CategoryTreeResponseDto treeDto = new CategoryTreeResponseDto(categoryId, "laptops", "Ноутбуки", List.of());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(updated));
        when(categoryMapper.toCategory(requestDto)).thenReturn(updated);
        when(categoryRepository.save(updated)).thenReturn(updated);
        when(categoryMapper.toCategoryTreeResponseDto(updated)).thenReturn(treeDto);

        CategoryTreeResponseDto result = categoryService.updateCategory(categoryId, requestDto);

        assertEquals("Ноутбуки", result.name());
        verify(categoryRepository).save(updated);
    }

    @Test
    void updateCategory_throws_whenCategoryNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(categoryId, new CategoryRequestDto(null, "x", "X", null)));
    }

    @Test
    void deleteCategory_deletesSuccessfully() {
        Category category = new Category("Планшеты", "tablets");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_throws_whenNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
    }

    @Test
    void getAllCategories_returnsListFromMapper() {
        Category root = new Category("Корень", "root");
        CategoryTreeResponseDto treeDto = new CategoryTreeResponseDto(UUID.randomUUID(), "root", "Корень", List.of());

        when(categoryRepository.findByParentIsNull()).thenReturn(List.of(root));
        when(categoryMapper.toCategoryTreeResponseDtoList(List.of(root))).thenReturn(List.of(treeDto));

        List<CategoryTreeResponseDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Корень", result.get(0).name());
    }
}
