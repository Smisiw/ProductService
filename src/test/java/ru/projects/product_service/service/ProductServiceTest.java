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
import java.util.UUID;
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

    private final UUID sellerId = UUID.fromString("bdb3d8e4-2b13-49e1-a042-02247558a707");

    @BeforeEach
    void setUp() {
        var auth = new UsernamePasswordAuthenticationToken(sellerId, null,
                List.of(new SimpleGrantedAuthority("ROLE_SELLER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createProduct_successForSeller_withMultipleAttributes() {
        ProductRequestDto requestDto = new ProductRequestDto(
                UUID.fromString("7ba1cdb1-2a83-435f-9024-36f069d1c7d5"),
                "Смартфон Samsung Galaxy",
                List.of(new VariationRequestDto(
                        "Смартфон Samsung Galaxy / 8GB RAM / 256GB ROM",
                        "Модель с отличной производительностью",
                        new BigDecimal("499.99"),
                        50,
                        0,
                        List.of(
                                new AttributeValueRequestDto(UUID.fromString("65418ffa-490d-4cef-86f0-52ca75bd983d"), "8GB"),
                                new AttributeValueRequestDto(UUID.fromString("b871dee2-4625-4b86-b6ca-9806b49b231e"), "256GB")
                        )
                ))
        );

        Category category = new Category(
                "Смартфоны",
                "smartphones"
        );
        category.setId(UUID.fromString("7ba1cdb1-2a83-435f-9024-36f069d1c7d5"));

        Attribute ramAttr = new Attribute("Оперативная память");
        ramAttr.setId(UUID.fromString("65418ffa-490d-4cef-86f0-52ca75bd983d"));

        Attribute storageAttr = new Attribute("Внутренняя память");
        storageAttr.setId(UUID.fromString("b871dee2-4625-4b86-b6ca-9806b49b231e"));

        ProductVariation variation = new ProductVariation(
                "Смартфон Samsung Galaxy / 8GB RAM / 256GB ROM",
                new BigDecimal("499.99"),
                50,
                0
        );
        variation.setDescription("Модель с отличной производительностью");
        variation.setAttributeValues(List.of(
                new AttributeValue("8GB", ramAttr),
                new AttributeValue("256GB", storageAttr)
        ));

        Product product = new Product(sellerId, category);
        product.setVariations(List.of(variation));

        ProductResponseDto productResponse = new ProductResponseDto(
                UUID.fromString("4a2d4efb-e2bc-47bc-9da5-4efd17229e04"),
                "Смартфон Samsung Galaxy",
                sellerId,
                new CategoryResponseDto(
                        category.getId(),
                        category.getRouteLocation(),
                        category.getName()
                ),
                List.of(
                        new VariationResponseDto(
                                UUID.fromString("737b8979-14f1-427c-8974-c8dc2a79c5bc"),
                                UUID.fromString("4a2d4efb-e2bc-47bc-9da5-4efd17229e04"),
                                variation.getName(),
                                variation.getDescription(),
                                variation.getQuantity(),
                                variation.getReserved(),
                                variation.getPrice(),
                                List.of(
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
                    p.setId(UUID.fromString("4a2d4efb-e2bc-47bc-9da5-4efd17229e04"));
                    for (ProductVariation v : p.getVariations()) {
                        v.setId(UUID.fromString("737b8979-14f1-427c-8974-c8dc2a79c5bc"));
                    }
                    return p;
                });
        when(productMapper.toProductResponseDto(product)).thenReturn(productResponse);

        ProductResponseDto response = productService.createProduct(requestDto);

        assertEquals("Смартфон Samsung Galaxy", response.name());
        assertEquals(sellerId, response.sellerId());
        assertEquals(UUID.fromString("7ba1cdb1-2a83-435f-9024-36f069d1c7d5"), response.category().id());
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