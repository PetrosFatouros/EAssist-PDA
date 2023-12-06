package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.entity.Inventory;
import com.unipi.pfatouros.eAssist_backend.service.implementation.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    // Use the corresponding service implementation to process incoming API requests
    private final InventoryServiceImpl inventoryService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public List<Inventory> getInventory() {

        return inventoryService.getInventory();
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Inventory getItemFromInventory(@PathVariable("id") Long id) {

        return inventoryService.getItemFromInventory(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public void addToInventory(@RequestBody ObjectNode inventoryItem) {

        inventoryService.addToInventory(
                inventoryItem.get("item_name").asText(),
                inventoryItem.get("category").asText(),
                inventoryItem.get("price").floatValue(),
                inventoryItem.get("quantity").asInt());
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void updateInventory(@PathVariable("id") Long id,
                                @RequestParam(required = false) String item_name,
                                @RequestParam(required = false) String category,
                                @RequestParam(required = false) Float price,
                                @RequestParam(required = false) Integer quantity) {

        inventoryService.updateInventory(id, item_name, category, price, quantity);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteFromInventory(@PathVariable("id") Long id) {

        inventoryService.deleteFromInventory(id);
    }
}
