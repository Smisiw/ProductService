package ru.projects.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.projects.product_service.DTO.AttributeValueResponseDto;
import ru.projects.product_service.model.AttributeValue;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {
    @Mapping(target = "attributeId", source = "attribute.id")
    @Mapping(target = "attributeName", source = "attribute.name")
    AttributeValueResponseDto toAttributeValueResponseDto(AttributeValue attributeValue);

    Set<AttributeValueResponseDto> toAttributeValueResponseDtoSet(Set<AttributeValue> attributeValues);
}
