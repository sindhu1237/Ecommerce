package com.app.ecomapplication.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Cart {
    private Long id;
    private Long userId;
    private LocalDate date;
    private List<CartItem> products;
}