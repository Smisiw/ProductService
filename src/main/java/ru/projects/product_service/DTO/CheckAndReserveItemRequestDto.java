package ru.projects.product_service.DTO;

import java.math.BigDecimal;

public record CheckAndReserveItemRequestDto(
        Long productVariationId,
        Integer quantity,
        BigDecimal unitPrice
) {
}
