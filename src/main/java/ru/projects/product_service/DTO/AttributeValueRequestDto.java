package ru.projects.product_service.DTO;

public record AttributeValueRequestDto(
        Long attributeId,
        String value
) {
}
