package com.example.EcommerceBackend.Controllers;

import com.example.EcommerceBackend.Services.CartService;
import com.example.EcommerceBackend.Entities.Cart;
import com.example.EcommerceBackend.Entities.CartItem;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addProductToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        try {
            Cart cart = cartService.addProductToCart(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeProductFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            Cart cart = cartService.removeProductFromCart(userId, productId);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCartItem(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        try {
            Cart cart = cartService.updateCartItem(userId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getCartItems(@RequestParam Long userId) {
        try {
            List<CartItem> items = cartService.getCartItems(userId);
            return ResponseEntity.ok(items);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart(@RequestParam Long userId) {
        try {
            Cart cart = cartService.clearCart(userId);
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}