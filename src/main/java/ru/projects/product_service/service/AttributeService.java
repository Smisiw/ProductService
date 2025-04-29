package ru.projects.product_service.service;

import ru.projects.product_service.model.Attribute;

import java.util.List;

public interface AttributeService {
    Attribute createAttribute(String name);
    Attribute getAttributeById(Long id);
    List<Attribute> getAllAttributes();
    Attribute updateAttribute(Long id, String name);
    void deleteAttribute(Long id);
}
