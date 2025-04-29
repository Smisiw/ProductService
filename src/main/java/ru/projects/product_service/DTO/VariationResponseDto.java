package ru.projects.product_service.DTO;

import java.math.BigDecimal;
import java.util.Set;

public record VariationResponseDto(
        Long id,
        Long productId,
        String name,
        String description,
        Integer quantity,
        BigDecimal price,
        Set<AttributeValueResponseDto> attributeValues
) {
}
