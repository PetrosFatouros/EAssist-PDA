package com.unipi.pfatouros.eassist.model;

import java.util.Set;

public class User {

    // User's app username (primary key)
    private String username;

    // User's email (unique)
    private final String email;

    // User's app password
    private final String password;

    // User's app roles (user can have many roles)
    private final Set<Role> roles;


    // Getters and Setters
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

    public Set<Role> getRoles() {
        return roles;
    }

    // Constructor
    public User(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
