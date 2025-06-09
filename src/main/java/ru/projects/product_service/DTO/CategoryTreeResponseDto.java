package ru.projects.product_service.DTO;

import java.util.List;
import java.util.UUID;

public record CategoryTreeResponseDto(
        UUID id,
        String routeLocation,
        String name,
        List<CategoryTreeResponseDto> children
) {
}
