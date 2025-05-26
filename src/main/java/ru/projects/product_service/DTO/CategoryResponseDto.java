package ru.projects.product_service.DTO;

public record CategoryResponseDto(
        Long id,
        String routeLocation,
        String name
) {
}
