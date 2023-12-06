package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.entity.Order;
import com.unipi.pfatouros.eAssist_backend.service.implementation.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {

    // Use the corresponding service implementation to process incoming API requests
    private final OrderServiceImpl orderService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Order> getOrders() {

        return orderService.getOrders();
    }

    @GetMapping(path = "/table/{table_id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Order> getOrders(@PathVariable("table_id") Long table_id,
                                 @RequestParam(required = false) String date) {

        if (date != null && date.trim().length() > 0) {
            return orderService.getOrders(table_id, date);
        } else {
            return orderService.getOrders(table_id);
        }
    }

    @GetMapping(path = "/status/{status}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Order> getOrdersBasedOnStatus(@PathVariable("status") String status,
                                              @RequestParam(required = false) String date) {

        if (date != null && date.trim().length() > 0) {
            return orderService.getOrders(status, date);
        } else {
            return orderService.getOrdersBasedOnStatus(status);
        }
    }

    @GetMapping(path = "/date/{date}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Order> getOrdersBasedOnDate(@PathVariable("date") String date) {

        return orderService.getOrdersBasedOnDate(date);
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void addOrder(@RequestBody ObjectNode order) {

        orderService.addOrder(order.get("table_id").asLong());
    }

    @PostMapping(path = "/full")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void addFullOrder(@RequestBody ObjectNode fullOrder) {

        // Get table id
        Long table_id = fullOrder.get("table_id").asLong();

        // Get inventory ids
        List<Long> inventoryIds = new ArrayList<>();
        for (JsonNode inventoryId : fullOrder.get("inventoryIds")) {
            inventoryIds.add(inventoryId.asLong());
        }

        // Get selected quantities
        List<Integer> selectedQuantities = new ArrayList<>();
        for (JsonNode selectedQuantity : fullOrder.get("selectedQuantities")) {
            selectedQuantities.add(selectedQuantity.asInt());
        }

        orderService.addFullOrder(table_id, inventoryIds, selectedQuantities);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void updateOrder(@PathVariable("id") Long id,
                            @RequestParam(required = false) Long table_id,
                            @RequestParam(required = false) String status) {

        orderService.updateOrder(id, table_id, status);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteOrder(@PathVariable("id") Long id) {

        orderService.deleteOrder(id);
    }
}
