package ru.projects.product_service.DTO;

import java.math.BigDecimal;
import java.util.Set;

public record VariationRequestDto(
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Integer reserved,
        Set<AttributeValueRequestDto> attributes
) {
}
