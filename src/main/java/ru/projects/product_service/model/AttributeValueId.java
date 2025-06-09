package ru.projects.product_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AttributeValueId implements Serializable {
    @Column(name = "attribute_id")
    private UUID attributeId;
    @Column(name = "variation_id")
    private UUID variationId;
}
