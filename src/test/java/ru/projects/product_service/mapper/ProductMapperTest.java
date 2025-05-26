package ru.projects.product_service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.projects.product_service.DTO.ProductRequestDto;
import ru.projects.product_service.DTO.ProductResponseDto;
import ru.projects.product_service.DTO.VariationRequestDto;
import ru.projects.product_service.exception.CategoryNotFoundException;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.CategoryRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private VariationMapper variationMapper;

    @InjectMocks
    private ProductMapperImpl productMapper;

    @Test
    void toProduct_mapsCorrectly_withVariations() {
        VariationRequestDto variationDto = new VariationRequestDto(
                "Вариант", null, new BigDecimal("100.0"), 5, 0, Set.of()
        );
        ProductRequestDto dto = new ProductRequestDto(1L, "Телевизор", Set.of(variationDto));

        Category category = new Category(
                "Электроника",
                "electronics"
        );
        category.setId(1L);

        ProductVariation variation = new ProductVariation(
                "Вариант",
                BigDecimal.valueOf(100.0),
                5,
                0
                );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(variationMapper.toProductVariationSet(Set.of(variationDto)))
                .thenReturn(Set.of(variation));

        Product product = productMapper.toProduct(dto, 99L);

        assertEquals("Телевизор", product.getName());
        assertEquals(99L, product.getSellerId());
        assertEquals(category, product.getCategory());
        assertEquals(1, product.getVariations().size());
        assertTrue(product.getVariations().contains(variation));
    }

    @Test
    void toProduct_throws_whenCategoryNotFound() {
        ProductRequestDto dto = new ProductRequestDto(100L, "Неизвестный", Set.of());

        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productMapper.toProduct(dto, 1L));
    }

    @Test
    void toProductResponseDto_shouldHandleNull() {
        ProductResponseDto dto = productMapper.toProductResponseDto(null);
        assertThat(dto).isNull();
    }
}