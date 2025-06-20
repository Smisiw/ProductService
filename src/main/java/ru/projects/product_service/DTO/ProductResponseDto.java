package ru.projects.product_service.DTO;

import java.util.Set;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        UUID sellerId,
        CategoryResponseDto category,
        Set<VariationResponseDto> variations
) {
}
