package ru.projects.product_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.projects.product_service.DTO.CategoryRequestDto;
import ru.projects.product_service.DTO.CategoryResponseDto;
import ru.projects.product_service.DTO.CategoryTreeResponseDto;
import ru.projects.product_service.model.Attribute;
import ru.projects.product_service.model.Category;
import ru.projects.product_service.repository.AttributeRepository;
import ru.projects.product_service.repository.CategoryRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttributeRepository attributeRepository;

    public abstract CategoryResponseDto toCategoryResponseDto(Category category);
    public abstract CategoryTreeResponseDto toCategoryTreeResponseDto(Category category);
    public Category toCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category(
                categoryRequestDto.name(),
                categoryRequestDto.routeLocation()
        );
        if (categoryRequestDto.parentId() != null) {
            Category parent = categoryRepository.findById(categoryRequestDto.parentId()).orElseThrow(
                    () -> new RuntimeException("Parent category with id " + categoryRequestDto.parentId() + " not found")
            );
            category.setParent(parent);
        }
        if (categoryRequestDto.attributeIds() != null) {
            for (Long attributeId : categoryRequestDto.attributeIds()) {
                Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(
                        () -> new RuntimeException("Attribute with id " + attributeId + " not found")
                );
                category.getAttributes().add(attribute);
            }
        }
        return category;
    }
    public abstract List<CategoryTreeResponseDto> toCategoryTreeResponseDtoList(List<Category> categoryList);
}
