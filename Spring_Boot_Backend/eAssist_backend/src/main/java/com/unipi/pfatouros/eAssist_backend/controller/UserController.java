package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.entity.User;
import com.unipi.pfatouros.eAssist_backend.service.implementation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    // Use the corresponding service implementation to process incoming API requests
    private final UserServiceImpl userService;

    @PostMapping("/sign_up")
    @PreAuthorize("hasRole('ADMIN')")
    public void registerUser(@RequestBody ObjectNode request) {

        // Get roles as strings from request
        Set<String> strRoles = new HashSet<>();
        for (JsonNode role : request.get("role")) {
            strRoles.add(role.asText());
        }

        userService.registerUser(
                request.get("username").asText(),
                request.get("email").asText(),
                request.get("password").asText(),
                strRoles
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {

        return userService.getUsers();
    }

    @GetMapping(path = "/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUser(@PathVariable("username") String username) {

        return userService.getUser(username);
    }

    @PutMapping(path = "/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(@PathVariable("username") String username,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) Set<String> roles) {

        userService.updateUser(username, email, password, roles);
    }

    @DeleteMapping(path = "/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("username") String username) {

        userService.deleteUser(username);
    }
}
