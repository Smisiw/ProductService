package ru.projects.product_service.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record VariationRequestDto(
        @NotNull(message = "variation name must not be null")
        String name,
        String description,
        @NotNull(message = "variation price must not be null")
        BigDecimal price,
        Integer quantity,
        Integer reserved,
        @Valid
        List<AttributeValueRequestDto> attributes
) {
}
