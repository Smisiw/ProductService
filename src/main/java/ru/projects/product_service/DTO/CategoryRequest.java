package ru.projects.product_service.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class CategoryRequest {
    @NotEmpty
    private String name;
    private Long parentId;
    private Set<Long> attributeIds;
}
