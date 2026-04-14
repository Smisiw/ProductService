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

import ru.projects.product_service.exception.NotRelevantProductInfoException;
import ru.projects.product_service.exception.ProductNotFoundException;
import ru.projects.product_service.exception.ProductReservationException;
import ru.projects.product_service.exception.RolePermissionExceprion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @Mock
    private jakarta.persistence.EntityManager entityManager;

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

    // ── getProductById ────────────────────────────────────────────────────────

    @Test
    void getProductById_returnsDto_whenFound() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(sellerId, new Category("Кат", "cat"));
        ProductResponseDto dto = new ProductResponseDto(productId, "Товар", sellerId,
                new CategoryResponseDto(UUID.randomUUID(), "cat", "Кат"), List.of());

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponseDto(product)).thenReturn(dto);

        ProductResponseDto result = productService.getProductById(productId);

        assertEquals(productId, result.id());
    }

    @Test
    void getProductById_throws_whenNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
    }

    // ── deleteProductById ─────────────────────────────────────────────────────

    @Test
    void deleteProductById_succeeds_whenAdmin() {
        UUID productId = UUID.randomUUID();
        var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                UUID.randomUUID(), null,
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN")));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

        Product product = new Product(UUID.randomUUID(), new Category("K", "k"));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProductById(productId);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductById_throws_whenSellerIsNotOwner() {
        UUID ownerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // setUp() already set sellerId as principal with ROLE_SELLER
        Product product = new Product(ownerId, new Category("K", "k"));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(RolePermissionExceprion.class, () -> productService.deleteProductById(productId));
    }

    // ── checkAndReserve ───────────────────────────────────────────────────────

    @Test
    void checkAndReserve_reservesSuccessfully() {
        UUID varId = UUID.randomUUID();
        ProductVariation variation = new ProductVariation("Вариант", new BigDecimal("100"), 10, 0);
        variation.setId(varId);

        CheckAndReserveItemRequestDto request = new CheckAndReserveItemRequestDto(varId, 3, new BigDecimal("100"));

        when(productVariationRepository.findAllById(List.of(varId))).thenReturn(List.of(variation));

        productService.checkAndReserve(List.of(request));

        assertEquals(3, variation.getReserved());
        verify(productVariationRepository).saveAll(List.of(variation));
    }

    @Test
    void checkAndReserve_throws_whenSizeMismatch() {
        UUID varId = UUID.randomUUID();
        CheckAndReserveItemRequestDto request = new CheckAndReserveItemRequestDto(varId, 1, new BigDecimal("100"));

        when(productVariationRepository.findAllById(List.of(varId))).thenReturn(List.of());

        assertThrows(NotRelevantProductInfoException.class, () -> productService.checkAndReserve(List.of(request)));
    }

    @Test
    void checkAndReserve_throws_whenPriceMismatch() {
        UUID varId = UUID.randomUUID();
        ProductVariation variation = new ProductVariation("Вариант", new BigDecimal("200"), 10, 0);
        variation.setId(varId);

        CheckAndReserveItemRequestDto request = new CheckAndReserveItemRequestDto(varId, 1, new BigDecimal("100"));
        when(productVariationRepository.findAllById(List.of(varId))).thenReturn(List.of(variation));

        assertThrows(NotRelevantProductInfoException.class, () -> productService.checkAndReserve(List.of(request)));
    }

    @Test
    void checkAndReserve_throws_whenInsufficientStock() {
        UUID varId = UUID.randomUUID();
        ProductVariation variation = new ProductVariation("Вариант", new BigDecimal("100"), 5, 3);
        variation.setId(varId);

        CheckAndReserveItemRequestDto request = new CheckAndReserveItemRequestDto(varId, 5, new BigDecimal("100"));
        when(productVariationRepository.findAllById(List.of(varId))).thenReturn(List.of(variation));

        assertThrows(NotRelevantProductInfoException.class, () -> productService.checkAndReserve(List.of(request)));
    }

    // ── cancelReservation ─────────────────────────────────────────────────────

    @Test
    void cancelReservation_decreasesReserved() {
        UUID varId = UUID.randomUUID();
        ProductVariation variation = new ProductVariation("Вариант", new BigDecimal("100"), 10, 5);
        variation.setId(varId);

        OrderItemCancelledEvent event = new OrderItemCancelledEvent(varId, 3);
        when(productVariationRepository.findById(varId)).thenReturn(Optional.of(variation));

        productService.cancelReservation(event);

        assertEquals(2, variation.getReserved());
        verify(productVariationRepository).save(variation);
    }

    @Test
    void cancelReservation_throws_whenVariationNotFound() {
        UUID varId = UUID.randomUUID();
        when(productVariationRepository.findById(varId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.cancelReservation(new OrderItemCancelledEvent(varId, 1)));
    }

    @Test
    void cancelReservation_throws_whenReservedLessThanQuantity() {
        UUID varId = UUID.randomUUID();
        ProductVariation variation = new ProductVariation("Вариант", new BigDecimal("100"), 10, 1);
        variation.setId(varId);

        when(productVariationRepository.findById(varId)).thenReturn(Optional.of(variation));

        assertThrows(ProductReservationException.class,
                () -> productService.cancelReservation(new OrderItemCancelledEvent(varId, 5)));
    }
}