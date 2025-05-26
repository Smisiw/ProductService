package ru.projects.product_service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.projects.product_service.DTO.AttributeValueRequestDto;
import ru.projects.product_service.DTO.VariationRequestDto;
import ru.projects.product_service.DTO.VariationResponseDto;
import ru.projects.product_service.exception.AttributeNotFoundException;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.model.AttributeValue;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.AttributeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VariationMapperTest {

    @Mock
    private AttributeRepository attributeRepository;
    @InjectMocks
    private VariationMapperImpl variationMapper;

    @Test
    void toProductVariation_successfulMapping_withAttributes() {
        VariationRequestDto dto = new VariationRequestDto(
                "Вариант A", "Описание", new BigDecimal("99.99"), 10, 2,
                Set.of(new AttributeValueRequestDto(1L, "Red"), new AttributeValueRequestDto(2L, "XL"))
        );

        Attribute attr1 = new Attribute("Цвет"); attr1.setId(1L);
        Attribute attr2 = new Attribute("Размер"); attr2.setId(2L);

        when(attributeRepository.findAllById(any()))
                .thenReturn(List.of(attr1, attr2));

        ProductVariation result = variationMapper.toProductVariation(dto);

        assertEquals("Вариант A", result.getName());
        assertEquals("Описание", result.getDescription());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        assertEquals(10, result.getQuantity());
        assertEquals(2, result.getReserved());

        assertEquals(2, result.getAttributeValues().size());
        Map<Long, String> attrMap = result.getAttributeValues().stream()
                .collect(Collectors.toMap(a -> a.getAttribute().getId(), AttributeValue::getValue));

        assertEquals("Red", attrMap.get(1L));
        assertEquals("XL", attrMap.get(2L));
    }

    @Test
    void toProductVariation_throws_whenAttributeMissing() {
        VariationRequestDto dto = new VariationRequestDto(
                "Тест", null, new BigDecimal("10.0"), 1, 0,
                Set.of(new AttributeValueRequestDto(99L, "???"))
        );

        when(attributeRepository.findAllById(List.of(99L)))
                .thenReturn(List.of());

        assertThrows(AttributeNotFoundException.class, () -> variationMapper.toProductVariation(dto));
    }

    @Test
    void toVariationResponseDto_shouldHandleNull() {
        VariationResponseDto dto = variationMapper.toVariationResponseDto(null);
        assertThat(dto).isNull();
    }

    @Test
    void toVariationResponseDtoSet_shouldHandleEmptyAndNull() {
        Set<VariationResponseDto> emptySet = variationMapper.toVariationResponseDtoSet(Set.of());
        assertThat(emptySet).isEmpty();

        Set<VariationResponseDto> nullSet = variationMapper.toVariationResponseDtoSet(null);
        assertThat(nullSet).isNull();
    }

    @Test
    void toProductVariationSet_shouldHandleEmptyAndNull() {
        Set<ProductVariation> emptySet = variationMapper.toProductVariationSet(Set.of());
        assertThat(emptySet).isEmpty();

        Set<ProductVariation> nullSet = variationMapper.toProductVariationSet(null);
        assertThat(nullSet).isNull();
    }
}