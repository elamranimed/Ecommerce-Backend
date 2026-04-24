package com.example.EcommerceBackend.Controllers;

import com.example.EcommerceBackend.Services.UserService;
import com.example.EcommerceBackend.Entities.User;
import com.example.EcommerceBackend.Entities.Cart;
import com.example.EcommerceBackend.Entities.Order;
import com.example.EcommerceBackend.Security.JwtUtil;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User registered = userService.registerUser(user);
            return ResponseEntity.ok(registered);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    return ResponseEntity.ok(new LoginResponse(token, user));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updated = userService.updateUser(id, user);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long id) {
        List<Order> orders = userService.getUserOrders(id);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<Cart> getUserCart(@PathVariable Long id) {
        Cart cart = userService.getUserCart(id);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO for login request
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // DTO for login response (token + user)
    public static class LoginResponse {
        private String token;
        private User user;

        public LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
    }
}