package com.example.EcommerceBackend.Repositories;

import com.example.EcommerceBackend.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
