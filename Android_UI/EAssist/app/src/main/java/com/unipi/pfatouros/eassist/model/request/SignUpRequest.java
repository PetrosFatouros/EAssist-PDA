package com.unipi.pfatouros.eassist.model.request;

import java.util.Set;

public class SignUpRequest {

    // User's app username (the one user types on add user fragment)
    private String username;

    // User's app email (the one user types on add user fragment)
    private final String email;

    // User's app password (the one user types on add user fragment)
    private final String password;

    // User's app roles (switchMaterials on add user fragment)
    private final Set<String> role;

    // Getters and Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRole() {
        return role;
    }

    // Constructor
    public SignUpRequest(String username, String email, String password, Set<String> role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
