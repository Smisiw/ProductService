package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.service.AttributeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Attribute> create(@RequestBody Attribute attribute) {
        return ResponseEntity.ok(attributeService.createAttribute(attribute.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Attribute>> getAll() {
        return ResponseEntity.ok(attributeService.getAllAttributes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attribute> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(attributeService.getAttributeById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Attribute> update(@PathVariable UUID id, @RequestBody Attribute attribute) {
        return ResponseEntity.ok(attributeService.updateAttribute(id, attribute.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.ok("Deleted");
    }
}
