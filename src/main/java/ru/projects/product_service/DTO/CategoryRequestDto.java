package ru.projects.product_service.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CategoryRequestDto(
        UUID parentId,
        @NotNull(message = "category routeLocation must not be null")
        String routeLocation,
        @NotNull(message = "category name must not be null")
        String name,
        Set<UUID> attributeIds
) {
}
