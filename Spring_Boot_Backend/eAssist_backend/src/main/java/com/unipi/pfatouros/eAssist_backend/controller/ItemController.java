package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.entity.Item;
import com.unipi.pfatouros.eAssist_backend.service.implementation.ItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/items")
@RequiredArgsConstructor
public class ItemController {

    // Use the corresponding service implementation to process incoming API requests
    private final ItemServiceImpl itemService;

    @GetMapping("/{order_id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Item> getItems(@PathVariable("order_id") Long order_id) {

        return itemService.getItems(order_id);
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void addItem(@RequestBody ObjectNode item) {

        itemService.addItem(item.get("order_id").asLong(),
                item.get("inventory_item_id").asLong(),
                item.get("selected_quantity").asInt());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void updateItem(@PathVariable("id") Long id,
                           @RequestParam(required = false) Long order_id,
                           @RequestParam(required = false) Long inventory_item_id) {

        itemService.updateItem(id, order_id, inventory_item_id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteItem(@PathVariable("id") Long id) {

        itemService.deleteItem(id);
    }
}
