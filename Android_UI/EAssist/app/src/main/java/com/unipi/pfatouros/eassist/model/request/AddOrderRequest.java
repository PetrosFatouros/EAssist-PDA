package com.unipi.pfatouros.eassist.model.request;

import java.util.List;

public class AddOrderRequest {

    // Id of the selected table
    private final Long table_id;

    // Ids of the selected inventory items
    private final List<Long> inventoryIds;

    // Times the corresponding items were selected
    private final List<Integer> selectedQuantities;

    // Getters
    public Long getTable_id() {
        return table_id;
    }

    public List<Long> getInventoryIds() {
        return inventoryIds;
    }

    public List<Integer> getSelectedQuantities() {
        return selectedQuantities;
    }

    // Constructor
    public AddOrderRequest(Long table_id,
                           List<Long> inventoryIds,
                           List<Integer> selectedQuantities) {
        this.table_id = table_id;
        this.inventoryIds = inventoryIds;
        this.selectedQuantities = selectedQuantities;
    }
}
