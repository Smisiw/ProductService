package ru.projects.product_service.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AttributeValueRequestDto(
        @NotNull(message = "attribute id must not be null")
        UUID attributeId,
        @NotNull(message = "attribute value must not be null")
        String value
) {
}
