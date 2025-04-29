package ru.projects.product_service.DTO;

import java.util.List;

public record CategoryTreeResponseDto(
        Long id,
        String name,
        List<CategoryTreeResponseDto> children
) {
}
