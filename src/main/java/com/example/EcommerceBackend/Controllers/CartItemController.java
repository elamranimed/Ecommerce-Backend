package com.example.EcommerceBackend.Controllers;

import com.example.EcommerceBackend.Services.CartItemService;
import com.example.EcommerceBackend.Entities.CartItem;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@AllArgsConstructor
public class CartItemController { // Changé Cart_itemController en CartItemController pour correspondre au code interne

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        CartItem added = cartItemService.addCartItem(cartItem);
        return ResponseEntity.ok(added);
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getCartItemsByCart(@PathVariable Long cartId) {
        List<CartItem> items = cartItemService.getCartItemsByCart(cartId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity) {
        try {
            CartItem updated = cartItemService.updateCartItem(cartId, productId, quantity);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartItemService.removeCartItem(cartId, productId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> clearCartItems(@PathVariable Long cartId) {
        cartItemService.clearCartItems(cartId);
        return ResponseEntity.noContent().build();
    }
}