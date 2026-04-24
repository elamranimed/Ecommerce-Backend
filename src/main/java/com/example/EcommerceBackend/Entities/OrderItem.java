package com.example.EcommerceBackend.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference("order-items")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("product-order-items")
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private Integer price;
}