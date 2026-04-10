package com.example.EcommerceBackend.Services;

import com.example.EcommerceBackend.Entities.Order;
import com.example.EcommerceBackend.Entities.OrderItem;
import com.example.EcommerceBackend.Entities.Cart;
import com.example.EcommerceBackend.Entities.CartItem;
import com.example.EcommerceBackend.Entities.User;
import com.example.EcommerceBackend.Repositories.OrderRepo;
import com.example.EcommerceBackend.Repositories.OrderItemRepo;
import com.example.EcommerceBackend.Repositories.CartRepo;
import com.example.EcommerceBackend.Repositories.CartItemRepo;
import com.example.EcommerceBackend.Repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {

    private final OrderRepo orderRepository;
    private final OrderItemRepo orderItemRepo;
    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepo;
    private final UserRepo userRepository;

    // -------------------- CRÉER UNE COMMANDE --------------------
    public Order placeOrder(Long userId) {
        // Récupérer le panier de l'utilisateur ou lancer une exception s'il n'existe pas
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Récupérer l'utilisateur (Admin ou Customer) pour l'associer à la commande
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Initialiser et sauvegarder une nouvelle entité commande pour l'utilisateur
        Order order = new Order();
        order.setUser(user);
        orderRepository.save(order);

        // Récupérer la liste des articles présents dans le panier
        List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());
        
        // Transformer chaque article du panier en un article de commande (copie des données)
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            // Persister l'article de commande en base de données
            orderItemRepo.save(orderItem);
        }

        // Supprimer définitivement les articles du panier après la validation de commande
        cartItemRepo.deleteAll(cartItems);

        log.info("Order created successfully for user {}", userId);
        return order;
    }

    // -------------------- LIRE LES COMMANDES --------------------
    
    // Utilisée par OrderController
    public List<Order> getUserOrders(Long userId) {
        // Rechercher l'historique complet des commandes d'un utilisateur spécifique
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long orderId) {
        // Rechercher une commande unique par son identifiant primaire
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    // -------------------- SUPPRIMER / ANNULER --------------------
    
    // Utilisée par OrderController
    public void cancelOrder(Long orderId) {
        // Vérifier l'existence de la commande avant de tenter la suppression
        if (!orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Order not found");
        }
        orderRepository.deleteById(orderId);
        log.info("Order {} cancelled successfully", orderId);
    }

    public void deleteOrder(Long orderId) {
        cancelOrder(orderId);
    }
}