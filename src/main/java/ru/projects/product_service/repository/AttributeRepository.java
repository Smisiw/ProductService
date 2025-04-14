package ru.projects.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.projects.product_service.model.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
