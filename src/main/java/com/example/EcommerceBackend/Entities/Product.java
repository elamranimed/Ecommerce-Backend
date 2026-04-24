package com.example.EcommerceBackend.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "products",
    uniqueConstraints = {
        @UniqueConstraint(name = "products_sku_key", columnNames = {"sku"})
    }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer price;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @Column(nullable = false)
    private Integer stock = 0;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("product-order-items")
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();
}