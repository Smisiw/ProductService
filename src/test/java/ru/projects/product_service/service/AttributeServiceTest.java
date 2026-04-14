package ru.projects.product_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.projects.product_service.exception.AttributeAlreadyExistException;
import ru.projects.product_service.exception.AttributeNotFoundException;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.repository.AttributeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private AttributeService attributeService;

    private final UUID attributeId = UUID.randomUUID();

    @Test
    void createAttribute_savesAndReturnsAttribute() {
        Attribute attribute = new Attribute("Цвет");
        when(attributeRepository.findByName("Цвет")).thenReturn(Optional.empty());
        when(attributeRepository.save(any(Attribute.class))).thenReturn(attribute);

        Attribute result = attributeService.createAttribute("Цвет");

        assertEquals("Цвет", result.getName());
        verify(attributeRepository).save(any(Attribute.class));
    }

    @Test
    void createAttribute_throws_whenAlreadyExists() {
        Attribute existing = new Attribute("Цвет");
        when(attributeRepository.findByName("Цвет")).thenReturn(Optional.of(existing));

        assertThrows(AttributeAlreadyExistException.class, () -> attributeService.createAttribute("Цвет"));
        verify(attributeRepository, never()).save(any());
    }

    @Test
    void getAttributeById_returnsAttribute_whenFound() {
        Attribute attribute = new Attribute("Размер");
        attribute.setId(attributeId);
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));

        Attribute result = attributeService.getAttributeById(attributeId);

        assertEquals(attributeId, result.getId());
        assertEquals("Размер", result.getName());
    }

    @Test
    void getAttributeById_throws_whenNotFound() {
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.empty());

        assertThrows(AttributeNotFoundException.class, () -> attributeService.getAttributeById(attributeId));
    }

    @Test
    void getAllAttributes_returnsAll() {
        List<Attribute> attributes = List.of(new Attribute("A"), new Attribute("B"));
        when(attributeRepository.findAll()).thenReturn(attributes);

        List<Attribute> result = attributeService.getAllAttributes();

        assertEquals(2, result.size());
    }

    @Test
    void updateAttribute_updatesNameAndSaves() {
        Attribute attribute = new Attribute("Старый");
        attribute.setId(attributeId);
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));
        when(attributeRepository.save(attribute)).thenReturn(attribute);

        Attribute result = attributeService.updateAttribute(attributeId, "Новый");

        assertEquals("Новый", result.getName());
        verify(attributeRepository).save(attribute);
    }

    @Test
    void updateAttribute_throws_whenNotFound() {
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.empty());

        assertThrows(AttributeNotFoundException.class, () -> attributeService.updateAttribute(attributeId, "Название"));
    }

    @Test
    void deleteAttribute_deletesSuccessfully() {
        Attribute attribute = new Attribute("Удаляемый");
        attribute.setId(attributeId);
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));

        attributeService.deleteAttribute(attributeId);

        verify(attributeRepository).delete(attribute);
    }

    @Test
    void deleteAttribute_throws_whenNotFound() {
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.empty());

        assertThrows(AttributeNotFoundException.class, () -> attributeService.deleteAttribute(attributeId));
    }
}
