package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.service.AttributeService;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @PostMapping
    public ResponseEntity<Attribute> create(@RequestBody Attribute attribute) {
        return ResponseEntity.ok(attributeService.createAttribute(attribute.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Attribute>> getAll() {
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attribute> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attribute> update(@PathVariable Long id, @RequestBody Attribute attribute) {
        return ResponseEntity.ok(attributeService.updateAttribute(id, attribute.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.ok("Deleted");
    }
}
