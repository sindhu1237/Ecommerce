package com.app.ecomapplication.service;

import com.app.ecomapplication.model.Cart;
import com.app.ecomapplication.model.CartItem;
import com.app.ecomapplication.repository.CartRepository;
import com.app.ecomapplication.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;

    private final String apiUrl =
            "https://fakestoreapi.com/carts";

    @BeforeEach
    void setup() {

        ReflectionTestUtils.setField(
                cartService,
                "apiUrl",
                apiUrl
        );

        CartItem item = new CartItem();
        item.setId(1L);
        item.setProductId(1L);
        item.setQuantity(2);

        cart = new Cart();
        cart.setId(1L);
        cart.setUserId(1L);
        cart.setDate(LocalDate.now());
        cart.setProducts(List.of(item));
    }

    @Test
    void shouldReturnAllCarts() {

        when(cartRepository.findAll())
                .thenReturn(List.of(cart));

        List<Cart> carts =
                cartService.getAllCarts();

        assertEquals(1, carts.size());

        verify(cartRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnCartById() {

        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));

        Cart result =
                cartService.getCartById(1L);

        assertNotNull(result);

        assertEquals(1L,
                result.getUserId());
    }

    @Test
    void shouldAddCart() {

        when(cartRepository.save(cart))
                .thenReturn(cart);

        Cart saved =
                cartService.addCart(cart);

        assertNotNull(saved);
    }

    @Test
    void shouldDeleteCart() {

        when(cartRepository.findById(1L))
                .thenReturn(Optional.of(cart));

        cartService.deleteCart(1L);

        verify(cartRepository, times(1))
                .delete(cart);
    }

    @Test
    void shouldSyncCartsFromApi() {

        Cart[] apiCarts = { cart };

        when(restTemplate.getForObject(
                apiUrl,
                Cart[].class
        )).thenReturn(apiCarts);

        when(cartRepository.saveAll(anyList()))
                .thenReturn(List.of(cart));

        List<Cart> carts =
                cartService.syncCartsFromApi();

        assertEquals(1, carts.size());

        verify(restTemplate, times(1))
                .getForObject(
                        apiUrl,
                        Cart[].class
                );
    }
}