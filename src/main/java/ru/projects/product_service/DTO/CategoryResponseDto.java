package ru.projects.product_service.DTO;

import java.util.UUID;

public record CategoryResponseDto(
        UUID id,
        String routeLocation,
        String name
) {
}
