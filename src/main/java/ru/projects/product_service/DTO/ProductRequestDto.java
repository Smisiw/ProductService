package ru.projects.product_service.DTO;

import java.util.Set;

public record ProductRequestDto(
        Long categoryId,
        String name,
        Set<VariationRequestDto> variations
) {
}
