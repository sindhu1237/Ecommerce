package com.app.ecomapplication.controller;
import com.app.ecomapplication.model.Category;
import com.app.ecomapplication.model.Product;
import com.app.ecomapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/sync")
    public List<Product> syncProducts() {
        return productService.syncProductsFromApi();
    }
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/limited")
    public List<Product> getProductsLimited(@RequestParam(defaultValue = "5") int limit) {
        return productService.getProductsLimited(limit);
    }
    @GetMapping("/sorted")
    public List<Product> getProductsSorted(@RequestParam(defaultValue = "desc") String sort) {
        return productService.getProductsSorted(sort);
    }
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return productService.getAllCategories();
    }
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    @GetMapping("/search")
    public Page<Product> searchProducts(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "price") String sortBy,
                                        @RequestParam(defaultValue = "asc") String direction) {
        return productService.searchProducts(keyword, page, size, sortBy, direction);
    }
    @GetMapping("/filter")
    public Page<Product> filterProducts(@RequestParam String category,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return productService.filterProductsByCategory(category, page, size);
    }
    @GetMapping("/search-filter")
    public Page<Product> searchAndFilterProducts(@RequestParam String keyword,
                                                 @RequestParam String category,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "price") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String direction) {
        return productService.searchAndFilterProducts(keyword, category, page, size, sortBy, direction);
    }
}