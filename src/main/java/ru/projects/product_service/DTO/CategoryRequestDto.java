package ru.projects.product_service.DTO;

import java.util.Set;

public record CategoryRequestDto(
        Long parentId,
        String name,
        Set<Long> attributeIds
) {
}
