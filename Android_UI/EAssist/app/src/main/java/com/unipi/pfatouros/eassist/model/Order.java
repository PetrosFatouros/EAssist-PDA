package com.unipi.pfatouros.eassist.model;

public class Order {

    private Long id; // Primary key

    private Table table; // Associated table

    private String status; // Current status of the order (e.g. pending, active, etc)

    private final String date; // Date the order was created

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    // Constructor
    public Order(Long id, Table table, String status, String date) {
        this.id = id;
        this.table = table;
        this.status = status;
        this.date = date;
    }

}
