package com.example.EcommerceBackend.Repositories;

import com.example.EcommerceBackend.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
}