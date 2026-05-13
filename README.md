# EcommerceBackend

## Description
EcommerceBackend est une API REST backend développée avec Spring Boot pour une application e-commerce. Elle gère les utilisateurs, les produits, les paniers, les commandes et les éléments de commande. L'authentification est sécurisée via JWT, et les données sont stockées dans une base de données PostgreSQL hébergée sur Supabase.

Ce projet fait partie d'un système e-commerce complet, avec un frontend Angular prévu pour interagir avec cette API.

## Fonctionnalités
- **Gestion des utilisateurs** : Inscription, connexion, mise à jour et suppression des comptes utilisateurs. Rôles : CUSTOMER et ADMIN.
- **Gestion des produits** : CRUD complet pour les produits (SKU, titre, description, prix, catégorie, image, stock).
- **Gestion du panier** : Ajout, suppression, mise à jour et vidage des articles dans le panier d'un utilisateur.
- **Gestion des commandes** : Placement de commandes à partir du panier, récupération des commandes utilisateur, annulation de commandes.
- **Authentification sécurisée** : Utilisation de JWT pour la génération de tokens d'accès.
- **Documentation API** : Intégration de SpringDoc OpenAPI pour une interface Swagger UI.

## Technologies utilisées
- **Java 17** : Langage de programmation.
- **Spring Boot 3.5.13** : Framework pour le développement d'applications Java.
- **Spring Data JPA** : Pour l'accès aux données et la persistance.
- **PostgreSQL** : Base de données relationnelle.
- **JWT (JJWT)** : Pour l'authentification et l'autorisation.
- **Lombok** : Pour réduire le code boilerplate.
- **Spring Security** : Pour la sécurité de l'application.
- **SpringDoc OpenAPI** : Pour la documentation automatique de l'API.
- **Maven** : Outil de gestion des dépendances et de build.

## Architecture
Le projet suit une architecture en couches typique de Spring Boot :
- **Controllers** : Gestion des endpoints REST (UserController, ProductController, CartController, OrderController, etc.).
- **Services** : Logique métier (UserService, ProductService, CartService, OrderService, etc.).
- **Repositories** : Accès aux données via JPA (UserRepo, ProductRepo, CartRepo, etc.).
- **Entities** : Modèles de données JPA (User, Product, Cart, CartItem, Order, OrderItem).
- **Security** : Configuration JWT et filtres d'authentification.

### Schéma de base de données
- **users** : id, email (unique), username, password, phone, role (CUSTOMER/ADMIN).
- **products** : id, sku (unique), title, description, price, category, thumbnail, stock.
- **carts** : id, user_id (FK vers users).
- **cart_items** : id, cart_id (FK vers carts), product_id (FK vers products), quantity.
- **orders** : id, user_id (FK vers users), created_at.
- **order_items** : id, order_id (FK vers orders), product_id (FK vers products), quantity, price.

Les relations sont gérées via JPA avec des annotations @OneToMany, @ManyToOne, etc.

## Endpoints API principaux
### Utilisateurs
- `POST /api/users/register` : Inscription d'un utilisateur.
- `POST /api/users/login` : Connexion et génération de token JWT.
- `GET /api/users/{id}` : Récupération d'un utilisateur.
- `PUT /api/users/{id}` : Mise à jour d'un utilisateur.
- `DELETE /api/users/{id}` : Suppression d'un utilisateur.
- `GET /api/users/{id}/orders` : Récupération des commandes d'un utilisateur.
- `GET /api/users/{id}/cart` : Récupération du panier d'un utilisateur.

### Produits
- `GET /api/products` : Liste de tous les produits.
- `GET /api/products/{id}` : Détails d'un produit.
- `POST /api/products` : Création d'un produit.
- `PUT /api/products/{id}` : Mise à jour d'un produit.
- `DELETE /api/products/{id}` : Suppression d'un produit.

### Panier
- `GET /api/users/{userId}/cart` : Récupération du panier d'un utilisateur.
- `POST /api/cart/add?userId={}&productId={}&quantity={}` : Ajout d'un produit au panier.
- `DELETE /api/cart/remove?userId={}&productId={}` : Suppression d'un produit du panier.
- `PUT /api/cart/update?userId={}&productId={}&quantity={}` : Mise à jour de la quantité d'un article.
- `GET /api/cart/items?userId={}` : Liste des articles du panier.
- `DELETE /api/cart/clear?userId={}` : Vidage du panier.

### Commandes
- `POST /api/orders/place?userId={}` : Placement d'une commande à partir du panier.
- `GET /api/orders/user/{userId}` : Récupération des commandes d'un utilisateur.
- `GET /api/orders/{orderId}` : Détails d'une commande.
- `PUT /api/orders/{orderId}/cancel` : Annulation d'une commande.

Pour une documentation complète, accédez à `/swagger-ui.html` après le démarrage de l'application.

## Instructions d'installation et de configuration
1. **Prérequis** :
   - Java 17 installé.
   - Maven installé.
   - Une instance PostgreSQL (ici configurée pour Supabase).

2. **Clonage du projet** :
   ```
   git clone <url-du-repo>
   cd EcommerceBackend
   ```

3. **Configuration de la base de données** :
   - Modifiez `src/main/resources/application.properties` si nécessaire pour pointer vers votre base de données.
   - Actuellement configuré pour Supabase avec les paramètres suivants :
     - URL : jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:6543/postgres
     - Utilisateur : postgres.xhunxfypmwadnhkbwcip
     - Mot de passe : Admin@1234.-
     - Dialecte : PostgreSQL.

4. **Build et exécution** :
   - Compilez avec Maven : `mvn clean compile`
   - Lancez l'application : `mvn spring-boot:run` ou `./mvnw spring-boot:run`
   - L'application démarre sur le port par défaut (8080).

5. **Test de l'API** :
   - Utilisez Postman ou curl pour tester les endpoints.
   - Accédez à la documentation Swagger : http://localhost:8080/swagger-ui.html

## Utilisation
- Inscrivez un utilisateur via `/api/users/register`.
- Connectez-vous pour obtenir un token JWT.
- Utilisez le token dans l'en-tête `Authorization: Bearer <token>` pour les requêtes authentifiées.
- Gérez les produits, paniers et commandes via les endpoints correspondants.

## Sécurité
- Les mots de passe sont stockés (supposément hashés dans UserService, vérifiez l'implémentation).
- JWT expire après une durée définie (configurable dans JwtUtil).
- Rôles utilisateur pour différencier CUSTOMER et ADMIN.

## Contribution
- Forkez le repo.
- Créez une branche pour vos modifications.
- Soumettez une pull request.

## Auteurs
- [Votre nom ou équipe]

## Licence
Ce projet est sous licence MIT.
