package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "product")
@EqualsAndHashCode(exclude = "product")
@Table(name = "product_variations")
@RequiredArgsConstructor
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    private Integer quantity;
    private Integer reserved;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "variation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttributeValue> attributeValues = new HashSet<>();

    public void setAttributeValues(Set<AttributeValue> attributes) {
        for (AttributeValue attribute : new HashSet<>(attributeValues)) {
            attribute.setVariation(null);
        }
        attributeValues.clear();
        for (AttributeValue attribute : attributes) {
            attributeValues.add(attribute);
            attribute.setVariation(this);
        }
    }
}