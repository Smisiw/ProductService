package ru.projects.product_service.DTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VariationResponseDto(
        UUID id,
        UUID productId,
        String name,
        String description,
        Integer quantity,
        Integer reserved,
        BigDecimal price,
        List<AttributeValueResponseDto> attributeValues
) {
}
