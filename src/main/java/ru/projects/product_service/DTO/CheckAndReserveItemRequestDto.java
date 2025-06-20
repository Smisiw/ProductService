package ru.projects.product_service.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record CheckAndReserveItemRequestDto(
        UUID productVariationId,
        Integer quantity,
        BigDecimal unitPrice
) {
}
