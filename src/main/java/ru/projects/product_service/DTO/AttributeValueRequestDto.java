package ru.projects.product_service.DTO;

import jakarta.validation.constraints.NotNull;

public record AttributeValueRequestDto(
        @NotNull(message = "attribute id must not be null")
        Long attributeId,
        @NotNull(message = "attribute value must not be null")
        String value
) {
}
