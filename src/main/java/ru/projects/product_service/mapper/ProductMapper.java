package ru.projects.product_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.projects.product_service.DTO.ProductRequestDto;
import ru.projects.product_service.DTO.ProductResponseDto;
import ru.projects.product_service.exception.CategoryNotFoundException;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {VariationMapper.class, CategoryMapper.class})
public abstract class ProductMapper {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VariationMapper variationMapper;

    public abstract ProductResponseDto toProductResponseDto(Product product);
    public Product toProduct(ProductRequestDto productRequestDto, UUID sellerId) {
        Category category = categoryRepository.findById(productRequestDto.categoryId()).orElseThrow(
                () -> new CategoryNotFoundException("Category not found")
        );
        Product product = new Product(
                sellerId,
                category
        );
        product.setName(productRequestDto.name());
        if (productRequestDto.variations() != null) {
            List<ProductVariation> variations = variationMapper.toProductVariationList(productRequestDto.variations());
            product.setVariations(variations);
        }
        return product;
    }
}
