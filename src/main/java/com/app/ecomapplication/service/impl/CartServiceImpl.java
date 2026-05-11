package com.app.ecomapplication.service.impl;

import com.app.ecomapplication.exception.CartNotFoundException;
import com.app.ecomapplication.model.Cart;
import com.app.ecomapplication.model.CartItem;
import com.app.ecomapplication.repository.CartRepository;
import com.app.ecomapplication.service.CartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {
    @Value("${cart.fakestore.api.url}")
    private String apiUrl;
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;
    public CartServiceImpl(CartRepository cartRepository,RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(
                                "Cart not found"));
    }
    @Override
    public List<Cart> getCartsLimited(int limit) {
        return cartRepository.findAll()
                .stream()
                .limit(limit)
                .toList();
    }
    @Override
    public List<Cart> getCartsSorted(String sort) {
        if (sort.equalsIgnoreCase("desc")) {
            return cartRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        return cartRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    @Override
    public List<Cart> getCartsByDateRange(String startDate, String endDate) {
        return cartRepository.findByDateBetween(
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }
    @Override
    public List<Cart> getCartsByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    @Override
    public Cart addCart(Cart cart) {
        for (CartItem item : cart.getProducts()) {
            item.setCart(cart);
        }
        return cartRepository.save(cart);
    }
    @Override
    public Cart updateCart(Long id, Cart cart) {
        Cart existingCart = getCartById(id);
        existingCart.setUserId(cart.getUserId());
        existingCart.setDate(cart.getDate());
        existingCart.getProducts().clear();
        for (CartItem item : cart.getProducts()) {
            item.setCart(existingCart);
            existingCart.getProducts().add(item);
        }
        return cartRepository.save(existingCart);
    }
    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartById(id);
        cartRepository.delete(cart);
    }
@Override
public List<Cart> syncCartsFromApi() {
    Cart[] apiCarts = restTemplate.getForObject(apiUrl, Cart[].class);
    if (apiCarts == null) {
        throw new CartNotFoundException("No carts found");
    }
    List<Cart> carts = Arrays.asList(apiCarts);
    for (Cart cart : carts) {
        System.out.println("CART: " + cart);
        System.out.println("PRODUCTS SIZE: " + cart.getProducts().size());
        for (CartItem item : cart.getProducts()) {
            item.setCart(cart);
        }
    }
    return cartRepository.saveAll(carts);
}
}