package com.unipi.pfatouros.eassist.model;

public class Item {

    private Long id; // Primary key

    private Order order; // Associated order

    private final Inventory inventory_item; // Associated inventory item

    private Integer selected_quantity; // Times the item is selected for current order

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Inventory getInventory_item() {
        return inventory_item;
    }

    public Integer getSelected_quantity() {
        return selected_quantity;
    }

    public void setSelected_quantity(Integer selected_quantity) {
        this.selected_quantity = selected_quantity;
    }

    public Item(Inventory inventory_item) {
        this.inventory_item = inventory_item;
        selected_quantity = 0;
    }
}
