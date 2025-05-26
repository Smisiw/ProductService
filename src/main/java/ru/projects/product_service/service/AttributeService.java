package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.projects.product_service.exception.AttributeAlreadyExistException;
import ru.projects.product_service.exception.AttributeNotFoundException;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.repository.AttributeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;

    @Transactional
    public Attribute createAttribute(String name) {
        attributeRepository.findByName(name).ifPresent(attribute -> {
            throw new AttributeAlreadyExistException("Attribute already exists");
        });
        Attribute attribute = new Attribute(name);
        return attributeRepository.save(attribute);
    }

    public Attribute getAttributeById(Long id) {
        return getAttributeOrThrow(id);
    }

    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Transactional
    public Attribute updateAttribute(Long id, String name) {
        Attribute attribute = getAttributeOrThrow(id);
        attribute.setName(name);
        return attributeRepository.save(attribute);
    }

    @Transactional
    public void deleteAttribute(Long id) {
        Attribute attribute = getAttributeOrThrow(id);
        attributeRepository.delete(attribute);
    }

    private Attribute getAttributeOrThrow(Long id) {
        return attributeRepository.findById(id).orElseThrow(
                () -> new AttributeNotFoundException("Attribute with id " + id + " not found")
        );
    }
}
