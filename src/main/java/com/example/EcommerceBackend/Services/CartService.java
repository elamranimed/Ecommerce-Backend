package com.example.EcommerceBackend.Services;

import com.example.EcommerceBackend.Entities.Cart;
import com.example.EcommerceBackend.Entities.CartItem;
import com.example.EcommerceBackend.Entities.Product;
import com.example.EcommerceBackend.Entities.User;
import com.example.EcommerceBackend.Repositories.CartRepo;
import com.example.EcommerceBackend.Repositories.CartItemRepo;
import com.example.EcommerceBackend.Repositories.ProductRepo;
import com.example.EcommerceBackend.Repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepository;
    private final UserRepo userRepository;

    // -------------------- GET OR CREATE CART --------------------
    public Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user); // ✅ fix critique
            return cartRepository.save(newCart);
        });
    }

    // -------------------- AJOUTER UN PRODUIT AU PANIER --------------------
    @Transactional
    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartItem existingItem = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepo.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItemRepo.save(item);
        }

        return cartRepository.findById(cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    // -------------------- SUPPRIMER UN PRODUIT DU PANIER --------------------
    @Transactional
    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cartItemRepo.deleteByCartIdAndProductId(cart.getId(), productId);

        return cartRepository.findById(cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    // -------------------- MODIFIER LA QUANTITÉ --------------------
    @Transactional
    public Cart updateCartItem(Long userId, Long productId, int newQuantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));

        item.setQuantity(newQuantity);
        cartItemRepo.save(item);

        return cartRepository.findById(cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    // -------------------- OBTENIR LES ARTICLES --------------------
    public List<CartItem> getCartItems(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        return cartItemRepo.findByCartId(cart.getId());
    }

    // -------------------- VIDER LE PANIER --------------------
    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cartItemRepo.deleteAll(cartItemRepo.findByCartId(cart.getId()));

        return cartRepository.findById(cart.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }
}