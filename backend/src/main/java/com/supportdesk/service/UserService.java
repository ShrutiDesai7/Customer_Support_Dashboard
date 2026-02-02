package com.supportdesk.service;

import com.supportdesk.model.User;
import com.supportdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user
     */
    public User registerUser(String email, String password, String firstName, String lastName, User.UserRole role) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // Note: In production, this should be hashed using BCryptPasswordEncoder
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Authenticate user by email and password
     */
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get all users with a specific role
     */
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * Get all active agents
     */
    public List<User> getActiveAgents() {
        return userRepository.findByRoleAndActive(User.UserRole.AGENT, true);
    }

    /**
     * Update user profile
     */
    public User updateUser(Long userId, String firstName, String lastName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setFirstName(firstName);
        user.setLastName(lastName);

        return userRepository.save(user);
    }

    /**
     * Deactivate user account
     */
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setActive(false);
        return userRepository.save(user);
    }

    /**
     * Activate user account
     */
    public User activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setActive(true);
        return userRepository.save(user);
    }
}
