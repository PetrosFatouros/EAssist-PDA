package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Validated
public interface UserService {

    // Declaration of methods which contain the business logic related to the User entity

    // Authenticate user and return jwt if user's credentials are valid
    String authenticateUser(@NotBlank String username, @NotBlank String password);

    // Register a new user
    void registerUser(@NotBlank @Size(min = 3, max = 20) String username,
                      @NotBlank @Size(max = 50) @Email String email,
                      @NotBlank @Size(min = 6, max = 40) String password,
                      @NotNull Set<String> roles);

    // Get a list that contains all existing users
    List<User> getUsers();

    // Get a specific user
    User getUser(@NotBlank @Size(min = 3, max = 20) String username);

    // Update an existing user
    void updateUser(@NotBlank @Size(min = 3, max = 20) String username,
                    @Size(max = 50) @Email String email,
                    @Size(min = 6, max = 40) String password,
                    Set<String> strRoles);

    // Delete an existing user
    void deleteUser(@NotBlank @Size(min = 3, max = 20) String username);
}
