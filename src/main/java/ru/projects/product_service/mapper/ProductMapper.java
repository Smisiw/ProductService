package ru.projects.product_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.projects.product_service.DTO.ProductRequestDto;
import ru.projects.product_service.DTO.ProductResponseDto;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.model.Product;
import ru.projects.product_service.model.ProductVariation;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {VariationMapper.class, CategoryMapper.class})
public abstract class ProductMapper {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VariationMapper variationMapper;

    public abstract ProductResponseDto toProductResponseDto(Product product);
    public Product toProduct(ProductRequestDto productRequestDto, Long sellerId) {
        Product product = new Product();
        product.setSellerId(sellerId);
        product.setName(productRequestDto.name());
        Category category = categoryRepository.findById(productRequestDto.categoryId()).orElseThrow(
                () -> new RuntimeException("Category not found")
        );
        product.setCategory(category);
        if (productRequestDto.variations() != null) {
            Set<ProductVariation> variations = variationMapper.toProductVariationSet(productRequestDto.variations());
            product.setVariations(variations);
        }
        return product;
    }
}
