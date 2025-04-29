package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.repository.AttributeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService{
    private final AttributeRepository attributeRepository;

    @Override
    @Transactional
    public Attribute createAttribute(String name) {
        attributeRepository.findByName(name).ifPresent(attribute -> {
            throw new RuntimeException("Attribute already exists");
        });
        Attribute attribute = new Attribute();
        attribute.setName(name);
        return attributeRepository.save(attribute);
    }

    @Override
    public Attribute getAttributeById(Long id) {
        return attributeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Attribute with id " + id + " not found")
        );
    }

    @Override
    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Override
    @Transactional
    public Attribute updateAttribute(Long id, String name) {
        Attribute attribute = attributeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Attribute with id " + id + " not found")
        );
        attribute.setName(name);
        return attributeRepository.save(attribute);
    }

    @Override
    @Transactional
    public void deleteAttribute(Long id) {
        attributeRepository.deleteById(id);
    }
}
