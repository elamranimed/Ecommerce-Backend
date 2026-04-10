package com.example.EcommerceBackend.Repositories;

import com.example.EcommerceBackend.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional; // Ajouté

import java.util.List;
import java.util.Optional; 

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrderId(Long orderId);

    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);

    @Modifying
    @Transactional
    void deleteByOrderIdAndProductId(Long orderId, Long productId);
}