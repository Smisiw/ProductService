package ru.projects.product_service.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ProductRequestDto(
        @NotNull
        Long categoryId,
        @NotNull
        String name,
        @Valid
        Set<VariationRequestDto> variations
) {
}
