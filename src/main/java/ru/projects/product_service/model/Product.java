package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "products")
@RequiredArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariation> variations = new HashSet<>();

    public void addVariation(ProductVariation variation) {
        variations.add(variation);
        variation.setProduct(this);
    }

    public void removeVariation(ProductVariation variation) {
        variation.setProduct(null);
        variations.remove(variation);
    }
}