package com.example.EcommerceBackend.Repositories;

import com.example.EcommerceBackend.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}