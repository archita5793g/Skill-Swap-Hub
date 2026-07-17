package com.archita.skillswaphub.controller;

import com.archita.skillswaphub.model.User;
import com.archita.skillswaphub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")   // Allow frontend (Live Server) to connect
public class UserController {

    @Autowired
    private UserService userService;

    // ============================================
    // POST /api/users/register
    // Register a new user
    // Body: { name, email, password }
    // ============================================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User saved = userService.register(user);
            Map<String, Object> res = new HashMap<>();
            res.put("message", "Registration successful!");
            res.put("user", saved);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // ============================================
    // POST /api/users/login
    // Login an existing user
    // Body: { email, password }
    // ============================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String email    = body.get("email");
            String password = body.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required."));
            }

            User user = userService.login(email, password);
            Map<String, Object> res = new HashMap<>();
            res.put("message", "Login successful!");
            res.put("user", user);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // ============================================
    // GET /api/users
    // Get all registered users
    // ============================================
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ============================================
    // GET /api/users/{id}
    // Get a single user by ID
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ============================================
    // GET /api/users/search?keyword=python
    // Search users by skill, name, or location
    // ============================================
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchByKeyword(keyword));
    }

    // ============================================
    // PUT /api/users/{id}
    // Update user profile (skills, bio, location, name)
    // Body: { name, bio, skills, wantToLearn, location }
    // ============================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateProfile(id, updatedUser);
            Map<String, Object> res = new HashMap<>();
            res.put("message", "Profile updated successfully!");
            res.put("user", user);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // ============================================
    // DELETE /api/users/{id}
    // Delete a single user by ID
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ============================================
    // DELETE /api/users/all
    // Delete ALL users from database
    // Clears all name, email, password records
    // ============================================
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok(Map.of("message", "All users deleted from database!"));
    }
}