package com.example.EcommerceBackend.Controllers;

import com.example.EcommerceBackend.Services.OrderItemService;
import com.example.EcommerceBackend.Entities.OrderItem;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@AllArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItem orderItem) {
        OrderItem added = orderItemService.addOrderItem(orderItem);
        return ResponseEntity.ok(added);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrder(@PathVariable Long orderId) {
        List<OrderItem> items = orderItemService.getOrderItemsByOrder(orderId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/order/{orderId}/product/{productId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long orderId, @PathVariable Long productId, @RequestParam int quantity, @RequestParam int price) {
        try {
            OrderItem updated = orderItemService.updateOrderItem(orderId, productId, quantity, price);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/order/{orderId}/product/{productId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable Long orderId, @PathVariable Long productId) {
        try {
            orderItemService.removeOrderItem(orderId, productId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}