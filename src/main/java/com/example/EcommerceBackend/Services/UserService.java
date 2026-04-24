package com.example.EcommerceBackend.Services;

import com.example.EcommerceBackend.Entities.User;
import com.example.EcommerceBackend.Entities.Cart;
import com.example.EcommerceBackend.Entities.CartItem;
import com.example.EcommerceBackend.Entities.Order;
import com.example.EcommerceBackend.Entities.OrderItem;
import com.example.EcommerceBackend.Enums.Role;
import com.example.EcommerceBackend.Repositories.UserRepo;
import com.example.EcommerceBackend.Repositories.CartRepo;
import com.example.EcommerceBackend.Repositories.CartItemRepo;
import com.example.EcommerceBackend.Repositories.OrderRepo;
import com.example.EcommerceBackend.Repositories.OrderItemRepo;

import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepository;
    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepo;
    private final OrderRepo orderRepository;
    private final OrderItemRepo orderItemRepo;
    private final PasswordEncoder passwordEncoder;

    // -------------------- AUTH --------------------

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already used!");
        }
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Fallback for existing plaintext passwords (for backwards compatibility while testing)
            if (password.equals(user.getPassword()) || passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    // -------------------- USER CRUD (Pour UserController) --------------------

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    // -------------------- CART --------------------

    public Cart getUserCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
    }

    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        CartItem item = new CartItem(); 
        item.setCart(cart);
        cartItemRepo.save(item);
        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = getUserCart(userId);
        cartItemRepo.deleteByCartIdAndProductId(cart.getId(), productId);
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(Long userId, Long productId, int newQuantity) {
        Cart cart = getUserCart(userId);
        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));
        item.setQuantity(newQuantity);
        cartItemRepo.save(item);
        return cartRepository.save(cart);
    }

    // -------------------- ORDERS --------------------

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order placeOrder(Long userId) {
        Cart cart = getUserCart(userId);
        User user = getUserById(userId);
        Order order = new Order(); 
        order.setUser(user);
        orderRepository.save(order);

        List<CartItem> items = cartItemRepo.findByCartId(cart.getId());
        for (CartItem item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItemRepo.save(orderItem);
        }

        cartItemRepo.deleteAll(items);
        return order;
    }
}