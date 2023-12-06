package com.unipi.pfatouros.eAssist_backend.service.implementation;

import com.unipi.pfatouros.eAssist_backend.entity.Inventory;
import com.unipi.pfatouros.eAssist_backend.entity.Item;
import com.unipi.pfatouros.eAssist_backend.entity.Order;
import com.unipi.pfatouros.eAssist_backend.repository.InventoryRepository;
import com.unipi.pfatouros.eAssist_backend.repository.ItemRepository;
import com.unipi.pfatouros.eAssist_backend.repository.OrderRepository;
import com.unipi.pfatouros.eAssist_backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    // Implementation of methods which contain the business logic related to the Item entity

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public List<Item> getItems(Long order_id) {

        // Select order from the database
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> {
                    String message = "Order with id " + order_id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Select all items from the database that correspond to selected order
        return itemRepository.findItemsByOrder(order);
    }

    @Override
    public void addItem(Long order_id, Long inventory_item_id, Integer selected_quantity) {

        // Select order from the database
        Order order = orderRepository.findById(order_id)
                .orElseThrow(() -> {
                    String message = "Order with id " + order_id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Select inventory item from the database
        Inventory inventoryItem = inventoryRepository.findById(inventory_item_id)
                .orElseThrow(() -> {
                    String message = "Item with id " + inventory_item_id + " does not exist in inventory!";
                    return new IllegalStateException(message);
                });

        // Create new item based on selected order, inventory item and selected quantity
        Item item = new Item(order, inventoryItem, selected_quantity);

        // Insert item into the database
        itemRepository.save(item);
    }

    @Transactional
    @Override
    public void updateItem(Long id, Long order_id, Long inventory_item_id) {

        // Select item from the database
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Item with id " + id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Update item's order id
        if (order_id != null && !Objects.equals(item.getOrder().getId(), order_id)) {

            Order order = orderRepository.findById(order_id)
                    .orElseThrow(() -> {
                        String message = "Order with id " + order_id + " does not exist!";
                        return new IllegalStateException(message);
                    });

            item.setOrder(order);
        }

        // Update item's inventory item id
        if (inventory_item_id != null && !Objects.equals(item.getInventory_item().getId(), inventory_item_id)) {

            Inventory inventoryItem = inventoryRepository.findById(inventory_item_id)
                    .orElseThrow(() -> {
                        String message = "Item with id " + inventory_item_id + " does not exist in inventory!";
                        return new IllegalStateException(message);
                    });

            item.setInventory_item(inventoryItem);
        }
    }

    @Override
    public void deleteItem(Long id) {

        // True if item exists, False otherwise
        boolean exists = itemRepository.existsById(id);

        // If item does not exist, throw exception
        if(!exists){
            String message = "Item with id " + id + " does not exist!";
            throw new IllegalStateException(message);
        }

        // Delete item from the database
        itemRepository.deleteById(id);
    }
}
