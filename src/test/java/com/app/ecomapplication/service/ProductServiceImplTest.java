package com.app.ecomapplication.service;

import com.app.ecomapplication.model.Product;
import com.app.ecomapplication.repository.ProductRepository;
import com.app.ecomapplication.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;

    private Product product2;

    private final String apiUrl =
            "https://fakestoreapi.com/products";

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                productService,
                "apiUrl",
                apiUrl
        );

        product1 = new Product();
        product1.setId(1L);
        product1.setTitle("iPhone");
        product1.setDescription("Apple Mobile");
        product1.setPrice(80000);
        product1.setCategory("Electronics");

        product2 = new Product();
        product2.setId(2L);
        product2.setTitle("MacBook");
        product2.setDescription("Apple Laptop");
        product2.setPrice(150000);
        product2.setCategory("Electronics");
    }

    @Test
    void shouldReturnAllProducts() {

        when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        List<Product> products =
                productService.getAllProducts();

        assertNotNull(products);

        assertEquals(2, products.size());

        verify(productRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnProductById() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product1));

        Product product =
                productService.getProductById(1L);

        assertNotNull(product);

        assertEquals("iPhone",
                product.getTitle());
    }

    @Test
    void shouldAddProduct() {

        when(productRepository.save(product1))
                .thenReturn(product1);

        Product savedProduct =
                productService.addProduct(product1);

        assertNotNull(savedProduct);

        assertEquals("iPhone",
                savedProduct.getTitle());
    }

    @Test
    void shouldDeleteProduct() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product1));

        productService.deleteProduct(1L);

        verify(productRepository, times(1))
                .delete(product1);
    }

    @Test
    void shouldSyncProductsFromApi() {

        Product[] apiProducts =
                { product1, product2 };

        when(restTemplate.getForObject(
                apiUrl,
                Product[].class
        )).thenReturn(apiProducts);

        when(productRepository.saveAll(anyList()))
                .thenReturn(Arrays.asList(apiProducts));

        List<Product> products =
                productService.syncProductsFromApi();

        assertNotNull(products);

        assertEquals(2, products.size());

        verify(restTemplate, times(1))
                .getForObject(
                        apiUrl,
                        Product[].class
                );

        verify(productRepository, times(1))
                .saveAll(anyList());
    }
}