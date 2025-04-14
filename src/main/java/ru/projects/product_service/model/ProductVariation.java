package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "product_variations")
@RequiredArgsConstructor
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Column(nullable = false)
    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_variation_attributes",
            joinColumns = @JoinColumn(name = "variation_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private List<AttributeValue> attributes;

}