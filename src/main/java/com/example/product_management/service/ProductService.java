package com.example.product_management.service;

import com.example.product_management.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ProductService {
    List<Product> getAllProducts();       

    List<Product> getAllProducts(Sort sort);
    
    Optional<Product> getProductById(Long id);
    
    Product saveProduct(Product product);
    
    void deleteProduct(Long id);
    
    Page<Product> searchProducts(String keyword, Pageable pageable); 

    List<Product> getProductsByCategory(String category);

    List<Product> searchProductsAdvanced(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Sort sort);

    List<String> getAllCategories();

    long getCountByCategory(String category);

    BigDecimal getTotalInventoryValue();

    BigDecimal getAveragePrice();

    List<Product> getLowStockProducts(int threshold);

    List<Product> getRecentProducts();

    long getTotalCount();
}
