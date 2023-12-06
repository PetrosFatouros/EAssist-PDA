package com.unipi.pfatouros.eAssist_backend.service.implementation;

import com.unipi.pfatouros.eAssist_backend.entity.Role;
import com.unipi.pfatouros.eAssist_backend.entity.User;
import com.unipi.pfatouros.eAssist_backend.repository.UserRepository;
import com.unipi.pfatouros.eAssist_backend.security.jwt.JwtUtility;
import com.unipi.pfatouros.eAssist_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Implementation of methods which contain the business logic related to the User entity

    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder encoder;

    @Override
    public String authenticateUser(String username, String password) {

        // Create Authentication object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Set user authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Return access token
        return jwtUtility.generateToken(authentication);
    }

    @Override
    public void registerUser(String username, String email, String password, Set<String> strRoles) {

        // Check if another user with the same username already exists in the database
        boolean existsByUsername = userRepository.existsById(username);
        if (existsByUsername) {
            String message = "Error: User with username " + username + " already exists!";
            throw new IllegalStateException(message);
        }

        // Check if another user with the same email already exists in the database
        boolean existsByEmail = userRepository.existsByEmail(email);
        if (existsByEmail) {
            String message = "Error: User with email " + email + " already exists!";
            throw new IllegalStateException(message);
        }

        // Set up user's roles
        Set<Role> roles;
        if (strRoles == null || strRoles.isEmpty()) {
            throw new RuntimeException("Error: Role not found.");
        } else {
            roles = roleService.getRolesFromRequest(strRoles);
        }

        // Create new user's account
        User user = new User(username, email, encoder.encode(password), roles);

        // Insert user into the database
        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {

        // Select all users from the database
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) {

        // Select user from the database based on his username
        return userRepository.findById(username)
                .orElseThrow(() -> {
                    String message = "User with username " + username + " does not exist!";
                    return new IllegalStateException(message);
                });
    }

    @Transactional
    @Override
    public void updateUser(String username, String email, String password, Set<String> strRoles) {

        // Select user from the database
        User user = userRepository.findById(username)
                .orElseThrow(() -> {
                    String message = "User with username " + username + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Update user's email
        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<User> userOptional = userRepository.findById(email);

            if (userOptional.isPresent()) {
                String message = "User with email " + email + " already exists!";
                throw new IllegalStateException(message);
            }

            user.setEmail(email);
        }

        // Update user's password
        if (password != null && password.length() > 6
                && !Objects.equals(user.getPassword(), password)) {
            user.setPassword(encoder.encode(password));
        }

        // Update user's roles
        if (strRoles != null) {
            Set<Role> roles = roleService.getRolesFromRequest(strRoles);

            System.out.println(roles);
            if (roles != null && !Objects.equals(user.getRoles(), roles)) {
                user.setRoles(roles);
            }
        }
    }

    @Override
    public void deleteUser(String username) {

        // True if user exists, False otherwise
        boolean exists = userRepository.existsById(username);

        // If user does not exist, throw exception
        if (!exists) {
            String message = "User with username " + username + " does not exist!";
            throw new IllegalStateException(message);
        }

        // Delete user from the database
        userRepository.deleteById(username);
    }
}
