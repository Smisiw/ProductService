package ru.projects.product_service.service;

import ru.projects.product_service.model.Attribute;

public interface AttributeService {
    Attribute createAttribute(String name);
    Attribute updateAttribute(Long id, String name);
    void deleteAttribute(Long id);
}
