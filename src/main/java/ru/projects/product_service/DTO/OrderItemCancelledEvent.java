package ru.projects.product_service.DTO;

public record OrderItemCancelledEvent(
        Long productVariationId,
        Integer quantity
) {
}
