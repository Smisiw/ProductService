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
import java.util.UUID;
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
                List.of(
                        new AttributeValueRequestDto(UUID.fromString("a041cfc7-2eb0-45b7-b2e6-aa008ad2f001"), "Red"),
                        new AttributeValueRequestDto(UUID.fromString("dc2a3f64-21f4-43fa-a06e-3c36fa9e8bad"), "XL")
                )
        );

        Attribute attr1 = new Attribute("Цвет"); attr1.setId(UUID.fromString("a041cfc7-2eb0-45b7-b2e6-aa008ad2f001"));
        Attribute attr2 = new Attribute("Размер"); attr2.setId(UUID.fromString("dc2a3f64-21f4-43fa-a06e-3c36fa9e8bad"));

        when(attributeRepository.findAllById(any()))
                .thenReturn(List.of(attr1, attr2));

        ProductVariation result = variationMapper.toProductVariation(dto);

        assertEquals("Вариант A", result.getName());
        assertEquals("Описание", result.getDescription());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        assertEquals(10, result.getQuantity());
        assertEquals(2, result.getReserved());

        assertEquals(2, result.getAttributeValues().size());
        Map<UUID, String> attrMap = result.getAttributeValues().stream()
                .collect(Collectors.toMap(a -> a.getAttribute().getId(), AttributeValue::getValue));

        assertEquals("Red", attrMap.get(UUID.fromString("a041cfc7-2eb0-45b7-b2e6-aa008ad2f001")));
        assertEquals("XL", attrMap.get(UUID.fromString("dc2a3f64-21f4-43fa-a06e-3c36fa9e8bad")));
    }

    @Test
    void toProductVariation_throws_whenAttributeMissing() {
        VariationRequestDto dto = new VariationRequestDto(
                "Тест", null, new BigDecimal("10.0"), 1, 0,
                List.of(new AttributeValueRequestDto(UUID.fromString("bdb3d8e4-2b13-49e1-a042-02247558a707"), "???"))
        );

        when(attributeRepository.findAllById(List.of(UUID.fromString("bdb3d8e4-2b13-49e1-a042-02247558a707"))))
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
        List<VariationResponseDto> emptySet = variationMapper.toVariationResponseDtoList(List.of());
        assertThat(emptySet).isEmpty();

        List<VariationResponseDto> nullSet = variationMapper.toVariationResponseDtoList(null);
        assertThat(nullSet).isNull();
    }

    @Test
    void toProductVariationSet_shouldHandleEmptyAndNull() {
        List<ProductVariation> emptySet = variationMapper.toProductVariationList(List.of());
        assertThat(emptySet).isEmpty();

        List<ProductVariation> nullSet = variationMapper.toProductVariationList(null);
        assertThat(nullSet).isNull();
    }
}