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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
                "Вариант", null, new BigDecimal("100.0"), 5, 0, List.of()
        );
        ProductRequestDto dto = new ProductRequestDto(UUID.fromString("707f1afa-adb2-45f2-aa8b-990c3842a183"), "Телевизор", List.of(variationDto));

        Category category = new Category(
                "Электроника",
                "electronics"
        );
        category.setId(UUID.fromString("707f1afa-adb2-45f2-aa8b-990c3842a183"));

        ProductVariation variation = new ProductVariation(
                "Вариант",
                BigDecimal.valueOf(100.0),
                5,
                0
                );

        when(categoryRepository.findById(UUID.fromString("707f1afa-adb2-45f2-aa8b-990c3842a183"))).thenReturn(Optional.of(category));
        when(variationMapper.toProductVariationList(List.of(variationDto)))
                .thenReturn(List.of(variation));

        Product product = productMapper.toProduct(dto, UUID.fromString("938d60e0-2830-44ea-8073-ec9b5e20ed8d"));

        assertEquals("Телевизор", product.getName());
        assertEquals(UUID.fromString("938d60e0-2830-44ea-8073-ec9b5e20ed8d"), product.getSellerId());
        assertEquals(category, product.getCategory());
        assertEquals(1, product.getVariations().size());
        assertTrue(product.getVariations().contains(variation));
    }

    @Test
    void toProduct_throws_whenCategoryNotFound() {
        ProductRequestDto dto = new ProductRequestDto(UUID.fromString("48c87913-3af6-4f6f-9c15-68510ec13ff3"), "Неизвестный", List.of());

        when(categoryRepository.findById(UUID.fromString("48c87913-3af6-4f6f-9c15-68510ec13ff3"))).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productMapper.toProduct(dto, UUID.fromString("707f1afa-adb2-45f2-aa8b-990c3842a183")));
    }

    @Test
    void toProductResponseDto_shouldHandleNull() {
        ProductResponseDto dto = productMapper.toProductResponseDto(null);
        assertThat(dto).isNull();
    }
}