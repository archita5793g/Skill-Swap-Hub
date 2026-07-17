package com.archita.skillswaphub.service;

import com.archita.skillswaphub.model.User;
import com.archita.skillswaphub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ============================================
    // REGISTER
    // ============================================
    public User register(User user) {
        // Check if email is already taken
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered! Please use a different email.");
        }
        // NOTE: In production use BCrypt to hash password
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ============================================
    // LOGIN
    // ============================================
    public User login(String email, String password) {
        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("No account found with this email.");
        }

        User user = userOpt.get();

        // Check password
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password. Please try again.");
        }

        return user;
    }

    // ============================================
    // GET ALL USERS
    // ============================================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ============================================
    // GET USER BY ID
    // ============================================
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // ============================================
    // UPDATE PROFILE
    // Updates: name, bio, skills, wantToLearn, location
    // Does NOT change email or password
    // ============================================
    public User updateProfile(Long id, User updatedUser) {
        User user = getUserById(id);

        if (updatedUser.getName()        != null) user.setName(updatedUser.getName());
        if (updatedUser.getBio()         != null) user.setBio(updatedUser.getBio());
        if (updatedUser.getSkills()      != null) user.setSkills(updatedUser.getSkills());
        if (updatedUser.getWantToLearn() != null) user.setWantToLearn(updatedUser.getWantToLearn());
        if (updatedUser.getLocation()    != null) user.setLocation(updatedUser.getLocation());

        return userRepository.save(user);
    }

    // ============================================
    // SEARCH USERS by skill keyword or name
    // ============================================
    public List<User> searchByKeyword(String keyword) {
        String kw = keyword.toLowerCase().trim();

        return userRepository.findAll()
                .stream()
                .filter(u ->
                        (u.getName()        != null && u.getName().toLowerCase().contains(kw))        ||
                                (u.getSkills()      != null && u.getSkills().toLowerCase().contains(kw))      ||
                                (u.getWantToLearn() != null && u.getWantToLearn().toLowerCase().contains(kw)) ||
                                (u.getLocation()    != null && u.getLocation().toLowerCase().contains(kw))
                )
                .collect(Collectors.toList());
    }

    // ============================================
    // DELETE SINGLE USER by ID
    // ============================================
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // ============================================
    // DELETE ALL USERS
    // Clears all name, email, password records
    // ============================================
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}