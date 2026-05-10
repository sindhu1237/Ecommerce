package com.app.ecomapplication.controller;

import com.app.ecomapplication.model.Cart;
import com.app.ecomapplication.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/")
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }
    @PostMapping("/sync")
    public List<Cart> syncCarts() {
        return cartService.syncCartsFromApi();
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @GetMapping("/limited")
    public List<Cart> getCartsLimited(@RequestParam(defaultValue = "5") int limit) {
        return cartService.getCartsLimited(limit);
    }

    @GetMapping("/sorted")
    public List<Cart> getCartsSorted(@RequestParam(defaultValue = "asc") String sort) {
        return cartService.getCartsSorted(sort);
    }

    @GetMapping("/date-range")
    public List<Cart> getCartsByDateRange(@RequestParam String startDate,@RequestParam String endDate) {
        return cartService.getCartsByDateRange(startDate, endDate);
    }

    @GetMapping("/user/{userId}")
    public List<Cart> getCartsByUserId(@PathVariable Long userId) {
        return cartService.getCartsByUserId(userId);
    }

    @PostMapping
    public Cart addCart(@RequestBody Cart cart) {
        return cartService.addCart(cart);
    }

    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable Long id, @RequestBody Cart cart) {
        return cartService.updateCart(id, cart);
    }

    @DeleteMapping("/{id}")
    public void deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
    }
}