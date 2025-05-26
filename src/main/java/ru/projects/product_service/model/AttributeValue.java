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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NonNull
    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    @NonNull
    private Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "variation_id")
    private ProductVariation variation;

}