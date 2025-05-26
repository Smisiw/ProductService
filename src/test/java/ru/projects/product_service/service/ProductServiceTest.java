package ru.projects.product_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.projects.product_service.DTO.*;
import ru.projects.product_service.mapper.ProductMapper;
import ru.projects.product_service.mapper.VariationMapper;
import ru.projects.product_service.model.*;
import ru.projects.product_service.repository.ProductRepository;
import ru.projects.product_service.repository.ProductVariationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductVariationRepository productVariationRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private VariationMapper variationMapper;

    @InjectMocks
    private ProductService productService;

    private final Long sellerId = 1L;

    @BeforeEach
    void setUp() {
        var auth = new UsernamePasswordAuthenticationToken(sellerId, null,
                List.of(new SimpleGrantedAuthority("ROLE_SELLER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createProduct_successForSeller_withMultipleAttributes() {
        ProductRequestDto requestDto = new ProductRequestDto(
                1L,
                "Смартфон Samsung Galaxy",
                Set.of(new VariationRequestDto(
                        "Смартфон Samsung Galaxy / 8GB RAM / 256GB ROM",
                        "Модель с отличной производительностью",
                        new BigDecimal("499.99"),
                        50,
                        0,
                        Set.of(
                                new AttributeValueRequestDto(1L, "8GB"),
                                new AttributeValueRequestDto(2L, "256GB")
                        )
                ))
        );

        Category category = new Category(
                "Смартфоны",
                "smartphones"
        );
        category.setId(1L);

        Attribute ramAttr = new Attribute("Оперативная память");
        ramAttr.setId(1L);

        Attribute storageAttr = new Attribute("Внутренняя память");
        storageAttr.setId(2L);

        ProductVariation variation = new ProductVariation(
                "Смартфон Samsung Galaxy / 8GB RAM / 256GB ROM",
                new BigDecimal("499.99"),
                50,
                0
        );
        variation.setDescription("Модель с отличной производительностью");
        variation.setAttributeValues(Set.of(
                new AttributeValue("8GB", ramAttr),
                new AttributeValue("256GB", storageAttr)
        ));

        Product product = new Product(sellerId, category);
        product.setVariations(Set.of(variation));

        ProductResponseDto productResponse = new ProductResponseDto(
                101L,
                "Смартфон Samsung Galaxy",
                sellerId,
                new CategoryResponseDto(
                        category.getId(),
                        category.getRouteLocation(),
                        category.getName()
                ),
                Set.of(
                        new VariationResponseDto(
                                201L,
                                101L,
                                variation.getName(),
                                variation.getDescription(),
                                variation.getQuantity(),
                                variation.getReserved(),
                                variation.getPrice(),
                                Set.of(
                                        new AttributeValueResponseDto(
                                                ramAttr.getId(),
                                                ramAttr.getName(),
                                                "8GB"
                                        ),
                                        new AttributeValueResponseDto(
                                                storageAttr.getId(),
                                                storageAttr.getName(),
                                                "256GB"
                                        )
                                )
                        )
                )
        );

        when(productMapper.toProduct(requestDto, sellerId)).thenReturn(product);
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    p.setId(101L);
                    for (ProductVariation v : p.getVariations()) {
                        v.setId(201L);
                        for (AttributeValue a : v.getAttributeValues()) {
                            a.setId(301L);
                        }
                    }
                    return p;
                });
        when(productMapper.toProductResponseDto(product)).thenReturn(productResponse);

        ProductResponseDto response = productService.createProduct(requestDto);

        assertEquals("Смартфон Samsung Galaxy", response.name());
        assertEquals(sellerId, response.sellerId());
        assertEquals(1L, response.category().id());
        assertEquals(1, response.variations().size());

        VariationResponseDto variationResponse = response.variations().iterator().next();
        assertEquals("Смартфон Samsung Galaxy / 8GB RAM / 256GB ROM", variationResponse.name());
        assertEquals("Модель с отличной производительностью", variationResponse.description());
        assertEquals(new BigDecimal("499.99"), variationResponse.price());
        assertEquals(2, variationResponse.attributeValues().size());

        Set<String> expectedAttributes = Set.of("Оперативная память: 8GB", "Внутренняя память: 256GB");
        Set<String> actualAttributes = variationResponse.attributeValues().stream()
                .map(a -> a.attributeName() + ": " + a.value())
                .collect(Collectors.toSet());

        assertEquals(expectedAttributes, actualAttributes);
    }

}