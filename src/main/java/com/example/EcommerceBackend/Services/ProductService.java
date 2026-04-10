package com.example.EcommerceBackend.Services;

import com.example.EcommerceBackend.Entities.Product;
import com.example.EcommerceBackend.Repositories.ProductRepo;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    private final ProductRepo productRepository;

    // -------------------- CRUD OPÉRATIONS --------------------

    // Créer un produit
    public Product createProduct(Product product) {
        // Vérifier si le SKU existe déjà en base pour éviter les doublons
        if (productRepository.findBySku(product.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU already exists!");
        }
        log.info("Creating product {}", product.getTitle());
        // Sauvegarder l'entité produit
        return productRepository.save(product);
    }

    // Lire tous les produits
    public List<Product> getAllProducts() {
        // Récupérer la liste complète des produits
        return productRepository.findAll();
    }

    // Lire un produit par ID
    public Optional<Product> getProductById(Long id) {
        // Rechercher un produit spécifique par sa clé primaire
        return productRepository.findById(id);
    }

    // Mettre à jour un produit
    public Product updateProduct(Long id, Product updatedProduct) {
        // Vérifier l'existence du produit avant modification
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Mettre à jour les champs un par un
        product.setTitle(updatedProduct.getTitle());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());
        product.setThumbnail(updatedProduct.getThumbnail());
        product.setStock(updatedProduct.getStock());

        log.info("Updating product {}", product.getTitle());
        // Persister les modifications
        return productRepository.save(product);
    }

    // Supprimer un produit
    public void deleteProduct(Long id) {
        // Vérifier si l'ID existe avant de tenter la suppression
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        log.info("Deleting product with id {}", id);
        productRepository.deleteById(id);
    }

    // -------------------- Logique MÉTIER  --------------------

    // Mettre à jour le stock après une commande
    public void updateStock(Long productId, int quantityOrdered) {
        // Récupérer le produit concerné
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Vérifier que la quantité demandée est disponible en stock
        if (product.getStock() < quantityOrdered) {
            throw new IllegalArgumentException("Insufficient stock for product " + product.getTitle());
        }

        // Déduire la quantité commandée du stock actuel
        product.setStock(product.getStock() - quantityOrdered);
        productRepository.save(product);
        log.info("Stock updated for product {}: new stock = {}", product.getTitle(), product.getStock());
    }
}