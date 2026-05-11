package com.app.ecomapplication.service;
import com.app.ecomapplication.model.Category;
import com.app.ecomapplication.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    List<Product> getProductsLimited(int limit);
    List<Product> getProductsSorted(String sort);
    List<Category> getAllCategories();
    List<Product> getProductsByCategory(String category);
    Product addProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    List<Product> syncProductsFromApi();
    Page<Product> searchProducts(String keyword, int page, int size, String sortBy, String direction);
    Page<Product> filterProductsByCategory(String category, int page, int size);
    Page<Product> searchAndFilterProducts(String keyword, String category, int page, int size, String sortBy, String direction);
}