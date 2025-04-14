package ru.projects.product_service.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class ProductVariationRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private Map<Long, String> attributeValues = new HashMap<>();
}
