package ru.projects.product_service.DTO;

import java.util.UUID;

public record AttributeValueResponseDto(
        UUID attributeId,
        String attributeName,
        String value
) {
}
