package ru.projects.product_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.OrderItemCancelledEvent;


@Service
@RequiredArgsConstructor
public class OrderEventListener {
    private final ProductService productService;

    @KafkaListener(topics = "order.item.cancelled", groupId = "product-service-group")
    public void handleOrderItemCancelled(String orderItemCancelledJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderItemCancelledEvent orderItemCancelledEvent;
        try {
            orderItemCancelledEvent = objectMapper.readValue(orderItemCancelledJSON, OrderItemCancelledEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        productService.cancelReservation(orderItemCancelledEvent);
        System.out.println("Product variation with id " + orderItemCancelledEvent.productVariationId() + " successfully unreserved");
    }
}
