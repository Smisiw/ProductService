package ru.projects.product_service.DTO;

import java.util.Set;

public record ProductResponseDto(
        Long id,
        String name,
        Long sellerId,
        CategoryResponseDto category,
        Set<VariationResponseDto> variations
) {
}
