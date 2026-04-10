package com.example.EcommerceBackend.Repositories;

import com.example.EcommerceBackend.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    void deleteByCartIdAndProductId(Long cartId, Long productId);
}