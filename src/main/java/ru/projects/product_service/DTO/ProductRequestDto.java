package ru.projects.product_service.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record ProductRequestDto(
        @NotNull
        UUID categoryId,
        @NotNull
        String name,
        @Valid
        Set<VariationRequestDto> variations
) {
}
