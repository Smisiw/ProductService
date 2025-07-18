package ru.projects.product_service.DTO;

import java.util.List;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        UUID sellerId,
        CategoryResponseDto category,
        List<VariationResponseDto> variations
) {
}
