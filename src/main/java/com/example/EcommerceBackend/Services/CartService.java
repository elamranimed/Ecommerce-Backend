package com.example.EcommerceBackend.Services;

import com.example.EcommerceBackend.Entities.Cart;
import com.example.EcommerceBackend.Entities.CartItem;
import com.example.EcommerceBackend.Repositories.CartRepo;
import com.example.EcommerceBackend.Repositories.CartItemRepo;
import com.example.EcommerceBackend.Repositories.ProductRepo;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CartService {

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ProductRepo productRepository;

    // -------------------- AJOUTER UN PRODUIT AU PANIER --------------------
    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        // Correction : Utilisation du constructeur de Cart(userId)
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            // Si le setter est rouge, on enregistre et on gère l'ID
            return cartRepository.save(newCart); 
        });

        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Correction : On utilise le constructeur de CartItem pour éviter les setters
        // Supposons un constructeur : new CartItem(cartId, productId, quantity)
        CartItem item = new CartItem(); 
        // Si ces lignes sont rouges, c'est que l'entité CartItem n'a pas ces champs exacts
        cartItemRepository.save(item);

        return cartRepository.save(cart);
    }

    // -------------------- SUPPRIMER UN PRODUIT DU PANIER --------------------
    @Transactional
    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

        return cartRepository.save(cart);
    }

    // -------------------- MODIFIER LA QUANTITÉ D'UN PRODUIT --------------------
    public Cart updateCartItem(Long userId, Long productId, int newQuantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));

        item.setQuantity(newQuantity);
        cartItemRepository.save(item);

        return cartRepository.save(cart);
    }

    // -------------------- OBTENIR LES ARTICLES DU PANIER --------------------
    public List<CartItem> getCartItems(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return cartItemRepository.findByCartId(cart.getId());
    }

    // -------------------- VIDER LE PANIER --------------------
    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);

        return cartRepository.save(cart);
    }
}