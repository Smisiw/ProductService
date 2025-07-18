package ru.projects.product_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.projects.product_service.DTO.VariationResponseDto;
import ru.projects.product_service.mapper.VariationMapper;
import ru.projects.product_service.model.AttributeValueDoc;
import ru.projects.product_service.model.ProductDoc;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.ProductElasticsearchRepository;
import ru.projects.product_service.repository.ProductVariationRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final ProductVariationRepository productVariationRepository;
    private final VariationMapper variationMapper;

    public Page<VariationResponseDto> searchProducts(String searchText, Pageable pageable) {
        Page<ProductDoc> searchResults = productElasticsearchRepository.searchByQuery(searchText, pageable);
        List<UUID> productIds = searchResults.getContent().stream().map(ProductDoc::getId).toList();
        List<VariationResponseDto> variationResponseDtos = variationMapper.toVariationResponseDtoList(productVariationRepository.findAllById(productIds).stream().toList());
        return new PageImpl<>(variationResponseDtos, pageable, searchResults.getTotalElements());
    }

    @Transactional
    public void initIndexes() {
        List<ProductVariation> variations = productVariationRepository.findAll();
        productElasticsearchRepository.saveAll(
                variations.stream().map(
                        variation -> new ProductDoc(
                                variation.getId(),
                                variation.getName(),
                                variation.getDescription(),
                                variation.getPrice(),
                                variation.getAttributeValues().stream().map(
                                        attributeValue -> new AttributeValueDoc(
                                                attributeValue.getAttribute().getName(),
                                                attributeValue.getValue()
                                        )
                                ).toList()
                        )
                ).toList()
        );
    }
}
