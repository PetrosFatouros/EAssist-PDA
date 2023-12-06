package com.unipi.pfatouros.eassist.model.request;

public class SignInRequest {

    // User's app username (the one user types on sign in fragment)
    private String username;

    // User's app password (the one user types on sign in fragment)
    private final String password;

    // Getters and Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // Constructor
    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
