package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.projects.product_service.DTO.AttributeRequest;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.service.AttributeService;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;

    @PostMapping
    public ResponseEntity<Attribute> create(@RequestBody AttributeRequest request) {
        return ResponseEntity.ok(attributeService.createAttribute(request.getName()));
    }
}
