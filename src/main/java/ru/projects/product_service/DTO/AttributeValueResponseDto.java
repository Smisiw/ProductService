package ru.projects.product_service.DTO;

public record AttributeValueResponseDto(
        Long attributeId,
        String attributeName,
        String value
) {
}
