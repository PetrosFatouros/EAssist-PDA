package com.unipi.pfatouros.eassist.model;

public class Table {

    private Long id; // Primary key

    private String name; // Name of the table

    private Boolean is_available; // True if table is available for new customers, false otherwise

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

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }

    // Constructor
    public Table(Long id, String name, Boolean is_available) {
        this.id = id;
        this.name = name;
        this.is_available = is_available;
    }
}
