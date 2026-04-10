package com.example.EcommerceBackend.Services;

import com.example.EcommerceBackend.Entities.OrderItem;
import com.example.EcommerceBackend.Repositories.OrderItemRepo;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OrderItemService {

    private final OrderItemRepo orderItemRepo;

    // -------------------- AJOUTER UN ARTICLE À LA COMMANDE --------------------
    public OrderItem addOrderItem(OrderItem orderItem) {
        // Sauvegarder un nouvel article dans une commande
        return orderItemRepo.save(orderItem);
    }

    // -------------------- OBTENIR LES ARTICLES D'UNE COMMANDE --------------------
    public List<OrderItem> getOrderItemsByOrder(Long orderId) {
        // Récupérer tous les articles d'une commande
        return orderItemRepo.findByOrderId(orderId);
    }

    // -------------------- METTRE À JOUR UN ARTICLE --------------------
    public OrderItem updateOrderItem(Long orderId, Long productId, int newQuantity, int newPrice) {
        // Mettre à jour la quantité et le prix d'un article dans une commande
        OrderItem item = orderItemRepo.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Order item not found"));
        item.setQuantity(newQuantity);
        item.setPrice(newPrice);
        return orderItemRepo.save(item);
    }

    // -------------------- SUPPRIMER UN ARTICLE --------------------
    public void removeOrderItem(Long orderId, Long productId) {
        // Supprimer un article spécifique d'une commande
        orderItemRepo.deleteByOrderIdAndProductId(orderId, productId);
    }
}