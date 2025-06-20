package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(exclude = "variation")
@EqualsAndHashCode(exclude = "variation")
@Table(name = "attribute_values")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttributeValue {

    @EmbeddedId
    private AttributeValueId id;

    @Column(nullable = false)
    @NonNull
    private String value;

    @ManyToOne(optional = false)
    @MapsId("attributeId")
    @JoinColumn(name = "attribute_id")
    @NonNull
    private Attribute attribute;

    @ManyToOne(optional = false)
    @MapsId("variationId")
    @JoinColumn(name = "variation_id")
    private ProductVariation variation;
}