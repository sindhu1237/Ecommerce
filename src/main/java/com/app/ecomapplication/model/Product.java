package com.app.ecomapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
        private Long id;
        private String title;
        private String description;
        private double price;
}
