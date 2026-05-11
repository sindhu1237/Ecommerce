package com.app.ecomapplication.service.impl;
import com.app.ecomapplication.exception.ProductNotFoundException;
import com.app.ecomapplication.model.Category;
import com.app.ecomapplication.model.Product;
import com.app.ecomapplication.repository.ProductRepository;
import com.app.ecomapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Value("${product.fakestore.api.url}")
    private String apiUrl;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    public ProductServiceImpl(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public List<Product> syncProductsFromApi() {
        Product[] apiProducts = restTemplate.getForObject(apiUrl, Product[].class);
        if (apiProducts == null) {
            throw new ProductNotFoundException("No products found");
        }
        List<Product> products = Arrays.asList(apiProducts);
        List<Product> newProducts = products.stream().filter(product -> productRepository.findByTitle(product.getTitle()).isEmpty()).peek(product -> product.setId(null)).toList();
        return productRepository.saveAll(newProducts);
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));
    }
    @Override
    public List<Product> getProductsLimited(int limit) {
        return productRepository.findAll()
                .stream()
                .limit(limit)
                .toList();
    }
    @Override
    public List<Product> getProductsSorted(String sort) {
        Sort sorting = sort.equalsIgnoreCase("desc")
                ? Sort.by("price").descending()
                : Sort.by("price").ascending();
        return productRepository.findAll(sorting);
    }
    @Override
    public List<Category> getAllCategories() {
        List<String> categories = productRepository.findAll()
                        .stream()
                        .map(Product::getCategory)
                        .distinct()
                        .toList();
        return categories.stream().map(Category::new).toList();
    }
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id).orElseThrow(() ->
                                new ProductNotFoundException("Product not found"));
        existing.setTitle(product.getTitle());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        return productRepository.save(existing);
    }
    @Override
    public void deleteProduct(Long id) {
        Product existing = productRepository.findById(id).orElseThrow(() ->
                                new ProductNotFoundException("Product not found"));
        productRepository.delete(existing);
    }
    @Override
    public Page<Product> searchProducts(String keyword, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }
    @Override
    public Page<Product> filterProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategoryIgnoreCase(category, pageable);
    }
    @Override
    public Page<Product> searchAndFilterProducts(String keyword, String category, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(keyword, category, pageable);
    }
}