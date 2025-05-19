package ru.projects.product_service.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CategoryRequestDto(
        Long parentId,
        @NotNull(message = "category name must not be null")
        String name,
        Set<Long> attributeIds
) {
}
