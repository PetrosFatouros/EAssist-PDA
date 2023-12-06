package com.unipi.pfatouros.eassist.model;

public class Inventory {

    // Primary key
    private Long id;

    // Name of the item
    private final String item_name;

    // Category of the item (e.g. drink, food, etc)
    private final String category;

    // Price of the item
    private Float price;

    // Item stock
    private Integer quantity;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getCategory() {
        return category;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Constructor
    public Inventory(Long id, String item_name, String category, Float price, Integer quantity) {
        this.id = id;
        this.item_name = item_name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}
