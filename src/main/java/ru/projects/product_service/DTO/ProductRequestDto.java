package ru.projects.product_service.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProductRequestDto(
        @NotNull
        UUID categoryId,
        @NotNull
        String name,
        @Valid
        List<VariationRequestDto> variations
) {
}
