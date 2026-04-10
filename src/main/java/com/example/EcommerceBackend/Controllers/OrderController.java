package com.example.EcommerceBackend.Controllers;

import com.example.EcommerceBackend.Services.OrderService;
import com.example.EcommerceBackend.Entities.Order;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}