package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.Inventory;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface InventoryService {

    // Declaration of methods which contain the business logic related to the Inventory entity

    // Get a list that contains all current inventory items
    List<Inventory> getInventory();

    // Get a specific inventory item
    Inventory getItemFromInventory(@NotNull Long id);

    // Add a new inventory item to the inventory
    void addToInventory(@NotBlank String item_name,
                        @NotBlank String category,
                        @NotNull Float price,
                        @NotNull Integer quantity);

    // Update an existing inventory item from the inventory
    void updateInventory(@NotNull Long id, String item_name, String category, Float price, Integer quantity);

    // Delete an existing inventory item from the inventory
    void deleteFromInventory(@NotNull Long id);
}
