package ru.projects.product_service.DTO;

import java.util.Set;

public record ProductResponseDto(
        Long id,
        String name,
        CategoryResponseDto category,
        Set<VariationResponseDto> variations
) {
}
