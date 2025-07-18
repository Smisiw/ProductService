package ru.projects.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.projects.product_service.DTO.AttributeValueRequestDto;
import ru.projects.product_service.DTO.VariationRequestDto;
import ru.projects.product_service.DTO.VariationResponseDto;
import ru.projects.product_service.exception.AttributeNotFoundException;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.model.AttributeValue;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.AttributeRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = AttributeValueMapper.class)
public abstract class VariationMapper {
    @Autowired
    private AttributeRepository attributeRepository;

    @Mapping(target = "productId", source = "product.id")
    public abstract VariationResponseDto toVariationResponseDto (ProductVariation productVariation);

    public ProductVariation toProductVariation (VariationRequestDto variationRequestDto) {
        ProductVariation productVariation = new ProductVariation(
                variationRequestDto.name(),
                variationRequestDto.price(),
                variationRequestDto.quantity() == null ? 0 : variationRequestDto.quantity(),
                variationRequestDto.reserved() == null ? 0 : variationRequestDto.reserved()
        );
        productVariation.setDescription(variationRequestDto.description() == null ? "" : variationRequestDto.description());

        if (variationRequestDto.attributes() != null) {
            List<UUID> attrIds = variationRequestDto.attributes().stream()
                    .map(AttributeValueRequestDto::attributeId)
                    .distinct()
                    .toList();

            Map<UUID, Attribute> attributeMap = attributeRepository.findAllById(attrIds)
                    .stream().collect(Collectors.toMap(Attribute::getId, Function.identity()));

            if (attributeMap.size() != attrIds.size()) {
                Set<UUID> foundIds = attributeMap.keySet();
                List<UUID> missingIds = attrIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .toList();
                throw new AttributeNotFoundException("Some attributes not found: " + missingIds);
            }

            List<AttributeValue> attributeValues = variationRequestDto.attributes().stream()
                    .map(attrDto -> {
                        Attribute attribute = attributeMap.get(attrDto.attributeId());
                        return new AttributeValue(attrDto.value(), attribute);
                    })
                    .toList();
            productVariation.setAttributeValues(attributeValues);
        }

        return productVariation;
    }

    public abstract Set<VariationResponseDto> toVariationResponseDtoSet (Set<ProductVariation> productVariations);
    public abstract List<VariationResponseDto> toVariationResponseDtoList (List<ProductVariation> productVariations);
    public abstract List<ProductVariation> toProductVariationList (List<VariationRequestDto> variationRequestDtos);
}
