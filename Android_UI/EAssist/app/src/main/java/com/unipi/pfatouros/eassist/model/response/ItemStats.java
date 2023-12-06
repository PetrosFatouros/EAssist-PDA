package com.unipi.pfatouros.eassist.model.response;

import com.unipi.pfatouros.eassist.model.Inventory;

public class ItemStats {

    private final Inventory inventory_item; // Associated inventory item

    private final Integer times_sold; // Times the item was sold

    private final Float profit; // Total profit the item made

    // Getters and Setters
    public Inventory getInventory_item() {
        return inventory_item;
    }

    public Integer getTimes_sold() {
        return times_sold;
    }

    public Float getProfit() {
        return profit;
    }

    // Constructor
    public ItemStats(Inventory inventory_item, Integer times_sold, Float profit) {
        this.inventory_item = inventory_item;
        this.times_sold = times_sold;
        this.profit = profit;
    }
}
