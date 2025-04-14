package ru.projects.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.projects.product_service.model.ProductVariation;


@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
    Page<ProductVariation> findByProductId(Long id, Pageable pageable);
}
