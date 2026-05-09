package com.app.ecomapplication.service.impl;

import com.app.ecomapplication.constants.FakeStoreApiConstants;
import com.app.ecomapplication.exception.CartNotFoundException;
import com.app.ecomapplication.model.Cart;
import com.app.ecomapplication.service.CartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Value("${cart.fakestore.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public CartServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Cart> getAllCarts() {
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(apiUrl, Cart[].class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return Arrays.asList(response.getBody());
    }

    @Override
    public Cart getCartById(Long id) {
        Cart cart = restTemplate.getForObject(apiUrl + "/" + id, Cart.class);
        if (cart == null) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return cart;
    }

    @Override
    public List<Cart> getCartsLimited(int limit) {
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(apiUrl + "?limit=" + limit, Cart[].class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<Cart> getCartsSorted(String sort) {
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(apiUrl + "?sort=" + sort, Cart[].class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<Cart> getCartsByDateRange(String startDate, String endDate) {
        String url = String.format("%s?startdate=%s&enddate=%s", apiUrl, startDate, endDate);
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(url, Cart[].class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return Arrays.asList(response.getBody());
    }

    @Override
    public List<Cart> getCartsByUserId(Long userId) {
        String url = String.format("%s/user/%d", apiUrl, userId);
        ResponseEntity<Cart[]> response = restTemplate.getForEntity(url, Cart[].class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return Arrays.asList(response.getBody());
    }

    @Override
    public Cart addCart(Cart cart) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cart> request = new HttpEntity<>(cart, headers);
        ResponseEntity<Cart> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Cart.class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return response.getBody();
    }

    @Override
    public Cart updateCart(Long id, Cart cart) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cart> request = new HttpEntity<>(cart, headers);
        ResponseEntity<Cart> response = restTemplate.exchange(apiUrl + id, HttpMethod.PUT, request, Cart.class);
        if (response.getStatusCode() != HttpStatusCode.valueOf(200)) {
            throw new CartNotFoundException(FakeStoreApiConstants.DATA_NOT_FOUND);
        }
        return response.getBody();
    }

    @Override
    public void deleteCart(Long id) {
        restTemplate.delete(apiUrl + id);
    }
}