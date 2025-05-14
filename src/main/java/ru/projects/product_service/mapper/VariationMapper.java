package ru.projects.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.projects.product_service.DTO.AttributeValueRequestDto;
import ru.projects.product_service.DTO.VariationRequestDto;
import ru.projects.product_service.DTO.VariationResponseDto;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.model.AttributeValue;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.AttributeRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = AttributeValueMapper.class)
public abstract class VariationMapper {
    @Autowired
    private AttributeRepository attributeRepository;

    @Mapping(target = "productId", source = "product.id")
    public abstract VariationResponseDto toVariationResponseDto (ProductVariation productVariation);

    public ProductVariation toProductVariation (VariationRequestDto variationRequestDto) {
        ProductVariation productVariation = new ProductVariation();
        productVariation.setName(variationRequestDto.name());
        productVariation.setDescription(variationRequestDto.description());
        productVariation.setPrice(variationRequestDto.price());
        productVariation.setQuantity(variationRequestDto.quantity());
        productVariation.setReserved(variationRequestDto.reserved());

        List<Long> attrIds = variationRequestDto.attributes().stream()
                .map(AttributeValueRequestDto::attributeId)
                .distinct()
                .toList();

        Map<Long, Attribute> attributeMap = attributeRepository.findAllById(attrIds)
                .stream().collect(Collectors.toMap(Attribute::getId, Function.identity()));

        if (attributeMap.size() != attrIds.size()) {
            Set<Long> foundIds = attributeMap.keySet();
            List<Long> missingIds = attrIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new RuntimeException("Some attributes not found: " + missingIds);
        }

        Set<AttributeValue> attributeValues = variationRequestDto.attributes().stream()
                .map(attrDto -> {
                    Attribute attribute = attributeMap.get(attrDto.attributeId());
                    AttributeValue value = new AttributeValue();
                    value.setAttribute(attribute);
                    value.setValue(attrDto.value());
                    return value;
                })
                .collect(Collectors.toSet());
        productVariation.setAttributeValues(attributeValues);
        return productVariation;
    }

    public abstract Set<VariationResponseDto> toVariationResponseDtoSet (Set<ProductVariation> productVariations);
    public abstract Set<ProductVariation> toProductVariationSet (Set<VariationRequestDto> variationRequestDtos);
    public abstract List<VariationResponseDto> toVariationResponseDtoList (List<ProductVariation> productVariations);
}
