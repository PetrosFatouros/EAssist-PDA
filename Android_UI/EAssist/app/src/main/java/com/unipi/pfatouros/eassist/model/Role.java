package com.unipi.pfatouros.eassist.model;

public class Role {

    // Primary key
    private Long id;

    // Role name
    String name;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Constructor
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
