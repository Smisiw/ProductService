package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter
@Setter
@ToString(exclude = "product")
@EqualsAndHashCode(exclude = "product")
@Table(name = "product_variations")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductVariation {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    @NonNull
    private String name;
    private String description;
    @Column(nullable = false)
    @NonNull
    private BigDecimal price;
    @Column(nullable = false)
    @NonNull
    private Integer quantity;
    @Column(nullable = false)
    @NonNull
    private Integer reserved;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "variation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeValue> attributeValues = new ArrayList<>();

    public void setAttributeValues(List<AttributeValue> attributes) {
        for (AttributeValue attribute : attributeValues) {
            attribute.setVariation(null);
        }
        attributeValues.clear();
        for (AttributeValue attribute : attributes) {
            attributeValues.add(attribute);
            attribute.setVariation(this);
        }
    }
}