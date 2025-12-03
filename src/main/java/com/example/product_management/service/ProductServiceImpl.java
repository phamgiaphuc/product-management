package com.example.product_management.service;

import com.example.product_management.entity.Product;
import com.example.product_management.repository.ProductRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
        
    @Override
    public List<Product> getAllProducts(Sort sort) {
        return productRepository.findAll(sort);
    }
    
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Product saveProduct(Product product) {
        // Validation logic can go here
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> searchProductsAdvanced(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Sort sort) {
        return productRepository.findAll(new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                
                if (name != null && !name.isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
                }
                if (category != null && !category.isEmpty()) {
                    predicates.add(cb.equal(root.get("category"), category));
                }
                if (minPrice != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
                }
                
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        }, sort);
    }

    @Override
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    @Override
    public long getCountByCategory(String category) {
        return productRepository.countByCategory(category);
    }

    @Override
    public BigDecimal getTotalInventoryValue() {
        BigDecimal value = productRepository.calculateTotalValue();
        return value != null ? value : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getAveragePrice() {
        BigDecimal avg = productRepository.calculateAveragePrice();
        return avg != null ? avg : BigDecimal.ZERO;
    }

    @Override
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    @Override
    public List<Product> getRecentProducts() {
        return productRepository.findTop5ByOrderByIdDesc();
    }

    @Override
    public long getTotalCount() {
        return productRepository.count();
    }
}
