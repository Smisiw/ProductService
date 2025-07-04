package ru.projects.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.projects.product_service.model.AttributeValue;
import ru.projects.product_service.model.AttributeValueId;


@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, AttributeValueId> {
}
