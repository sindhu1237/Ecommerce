package com.app.ecomapplication.config;

import com.app.ecomapplication.service.CartService;
import com.app.ecomapplication.service.ProductService;
import com.app.ecomapplication.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner run(
            ProductService productService,
            UserService userService,
            CartService cartService
    ) {

        return args -> {

            System.out.println("Starting FakeStore sync...");

            productService.syncProductsFromApi();

            userService.syncUsersFromApi();

            cartService.syncCartsFromApi();

            System.out.println("FakeStore sync completed.");
        };
    }
}