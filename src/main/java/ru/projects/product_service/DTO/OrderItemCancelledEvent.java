package ru.projects.product_service.DTO;

import java.util.UUID;

public record OrderItemCancelledEvent(
        UUID productVariationId,
        Integer quantity
) {
}
