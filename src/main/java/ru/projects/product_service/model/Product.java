package ru.projects.product_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "products")
@RequiredArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariation> variations = new HashSet<>();

    public void setVariations(Set<ProductVariation> variations) {
        for (ProductVariation variation : new HashSet<>(this.variations)) {
            variation.setProduct(null);
        }
        this.variations.clear();
        for (ProductVariation variation : variations) {
            this.variations.add(variation);
            variation.setProduct(this);
        }
    }

    public void addVariation(ProductVariation variation) {
        variations.add(variation);
        variation.setProduct(this);
    }

    public void removeVariation(ProductVariation variation) {
        variation.setProduct(null);
        variations.remove(variation);
    }
}