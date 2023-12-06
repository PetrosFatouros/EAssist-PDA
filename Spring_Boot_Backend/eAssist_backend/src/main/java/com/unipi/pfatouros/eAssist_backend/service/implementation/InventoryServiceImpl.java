package com.unipi.pfatouros.eAssist_backend.service.implementation;

import com.unipi.pfatouros.eAssist_backend.entity.Inventory;
import com.unipi.pfatouros.eAssist_backend.repository.InventoryRepository;
import com.unipi.pfatouros.eAssist_backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    // Implementation of methods which contain the business logic related to the Inventory entity

    private final InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getInventory() {

        // Select all inventory items from the database
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getItemFromInventory(Long id) {

        // Select inventory item from the database based on its item name
        return inventoryRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Item with id " + id + " does not exist in inventory!";
                    return new IllegalStateException(message);
                });
    }

    @Override
    public void addToInventory(String item_name, String category, Float price, Integer quantity) {

        // Check if another inventory item with the same item name already exists in the database
        Optional<Inventory> inventoryItemOptional = inventoryRepository.findAll()
                .stream().filter(inventory_item -> inventory_item.getItem_name().equals(item_name)).findFirst();

        if (inventoryItemOptional.isPresent()) {
            String message = "Error: Item with name " + item_name + " already exists in inventory!";
            throw new IllegalStateException(message);
        }

        // Create new inventory item
        Inventory inventoryItem = new Inventory(item_name, category, price, quantity);

        // Insert inventory item into the database
        inventoryRepository.save(inventoryItem);
    }

    @Transactional
    @Override
    public void updateInventory(Long id, String item_name, String category, Float price, Integer quantity) {

        // Select inventory item from the database
        Inventory inventoryItem = inventoryRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Item with id " + id + " does not exist in inventory!";
                    return new IllegalStateException(message);
                });

        // Update inventory item's name
        if (item_name != null && item_name.length() > 0 && !Objects.equals(inventoryItem.getItem_name(), item_name)) {

            // Check if another inventory item with the same item name already exists in the database
            Optional<Inventory> inventoryItemOptional = inventoryRepository.findAll()
                    .stream().filter(inventory_item -> inventory_item.getItem_name().equals(item_name)).findFirst();

            if (inventoryItemOptional.isPresent()) {
                String message = "Error: Item with name " + item_name + " already exists in inventory!";
                throw new IllegalStateException(message);
            }

            inventoryItem.setItem_name(item_name);
        }

        // Update inventory item's category
        if (category != null && category.length() > 0 && !Objects.equals(inventoryItem.getCategory(), category)) {
            inventoryItem.setCategory(category);
        }

        // Update inventory item's price
        if (price != null && !Objects.equals(inventoryItem.getPrice(), price)) {
            inventoryItem.setPrice(price);
        }

        // Update inventory item's quantity
        if (quantity != null && !Objects.equals(inventoryItem.getQuantity(), quantity)) {
            inventoryItem.setQuantity(quantity);
        }
    }

    @Override
    public void deleteFromInventory(Long id) {

        // True if inventory item exists, False otherwise
        boolean exists = inventoryRepository.existsById(id);

        // If inventory item does not exist, throw exception
        if (!exists) {
            String message = "Item with id " + id + " does not exist in inventory!";
            throw new IllegalStateException(message);
        }

        // Delete inventory item from the database
        inventoryRepository.deleteById(id);
    }
}
