package com.unipi.pfatouros.eAssist_backend.service.implementation;


import com.unipi.pfatouros.eAssist_backend.entity.*;
import com.unipi.pfatouros.eAssist_backend.repository.InventoryRepository;
import com.unipi.pfatouros.eAssist_backend.repository.ItemRepository;
import com.unipi.pfatouros.eAssist_backend.repository.OrderRepository;
import com.unipi.pfatouros.eAssist_backend.repository.TableRepository;
import com.unipi.pfatouros.eAssist_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    // Implementation of methods which contain the business logic related to the Order entity

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public OrderEnum getStatusFromRequest(String status) {

        // Convert String to OrderEnum
        switch (status) {
            case "active" -> {
                return OrderEnum.ACTIVE;
            }
            case "ready" -> {
                return OrderEnum.READY;
            }
            case "pending" -> {
                return OrderEnum.PENDING;
            }
            case "unpaid" -> {
                return OrderEnum.UNPAID;
            }
            case "complete" -> {
                return OrderEnum.COMPLETE;
            }
            default -> throw new RuntimeException("Error: Status not found.");
        }
    }

    @Override
    public List<Order> getOrders() {

        // Select all orders from the database
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrders(Long table_id) {

        // Select table from the database
        TableEntity table = tableRepository.findById(table_id)
                .orElseThrow(() -> {
                    String message = "Table with id " + table_id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Select all orders from the database that correspond to selected table
        return orderRepository.findOrdersByTable(table);
    }

    @Override
    public List<Order> getOrdersBasedOnStatus(String status) {

        // Select orders from the database based on status
        OrderEnum enum_status = getStatusFromRequest(status);
        if (enum_status.equals(OrderEnum.PENDING)) {

            List<Order> active_orders = orderRepository.findAll().stream()
                    .filter(order -> order.getStatus().equals(OrderEnum.ACTIVE)).toList();

            List<Order> ready_orders = orderRepository.findAll().stream()
                    .filter(order -> order.getStatus().equals(OrderEnum.READY)).toList();

            List<Order> unpaid_orders = orderRepository.findAll().stream()
                    .filter(order -> order.getStatus().equals(OrderEnum.UNPAID)).toList();

            List<Order> temp_list = Stream.concat(active_orders.stream(), ready_orders.stream()).toList();

            return Stream.concat(temp_list.stream(), unpaid_orders.stream()).collect(Collectors.toList());
        } else {
            return orderRepository.findAll().stream()
                    .filter(order -> order.getStatus().equals(getStatusFromRequest(status))).toList();
        }
    }

    @Override
    public List<Order> getOrdersBasedOnDate(String date) {

        // Select orders from the database based on date
        return orderRepository.findOrdersByDate(Date.valueOf(date));
    }


    @Override
    public List<Order> getOrders(Long table_id, String date) {

        // Select table from the database
        TableEntity table = tableRepository.findById(table_id)
                .orElseThrow(() -> {
                    String message = "Table with id " + table_id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Select all orders of given date from the database that correspond to selected table
        return orderRepository.findOrdersByTableAndDate(table, Date.valueOf(date));
    }

    @Override
    public List<Order> getOrders(String status, String date) {

        // Select orders from the database based on status and date
        OrderEnum enum_status = getStatusFromRequest(status);
        if (enum_status.equals(OrderEnum.PENDING)) {

            List<Order> active_orders = orderRepository.findOrdersByDate(Date.valueOf(date)).stream()
                    .filter(order -> order.getStatus().equals(OrderEnum.ACTIVE)).toList();

            List<Order> ready_orders = orderRepository.findOrdersByDate(Date.valueOf(date)).stream()
                    .filter(order -> order.getStatus().equals(OrderEnum.READY)).toList();

            List<Order> unpaid_orders = orderRepository.findOrdersByDate(Date.valueOf(date)).stream()
                    .filter(order -> order.getStatus().equals(OrderEnum.UNPAID)).toList();

            List<Order> temp_list = Stream.concat(active_orders.stream(), ready_orders.stream()).toList();

            return Stream.concat(temp_list.stream(), unpaid_orders.stream()).collect(Collectors.toList());
        } else {
            return orderRepository.findOrdersByDate(Date.valueOf(date)).stream()
                    .filter(order -> order.getStatus().equals(getStatusFromRequest(status))).toList();
        }
    }

    @Override
    public void addOrder(Long table_id) {

        // Select table from the database
        TableEntity table = tableRepository.findById(table_id)
                .orElseThrow(() -> {
                    String message = "Table with id " + table_id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Create new order based on selected table
        Order order = new Order(table, OrderEnum.ACTIVE, new Date(System.currentTimeMillis()));

        // Insert order into the database
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void addFullOrder(Long table_id, List<Long> inventoryIds, List<Integer> selectedQuantities) {

        // Select table from the database
        TableEntity table = tableRepository.findById(table_id)
                .orElseThrow(() -> {
                    String message = "Table with id " + table_id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Create new order based on selected table
        Order order = new Order(table, OrderEnum.ACTIVE, new Date(System.currentTimeMillis()));

        // Create a list with all order's items
        List<Item> items = new ArrayList<>();

        // Lengths must match
        if (inventoryIds.size() == selectedQuantities.size()) {

            // Get the size of the list
            int length = inventoryIds.size();

            for (int i = 0; i < length; i++) {

                // Variable must be final or effective final to be used in lambda expression
                int position = i;

                // Select inventory item from the database
                Inventory inventoryItem = inventoryRepository.findById(inventoryIds.get(i))
                        .orElseThrow(() -> {
                            String message = "Item with id " +
                                    inventoryIds.get(position) +
                                    " does not exist in inventory!";
                            return new IllegalStateException(message);
                        });

                // Create new item based on selected order, inventory item and selected quantity
                Item item = new Item(order, inventoryItem, selectedQuantities.get(i));

                // Add item to list
                items.add(item);
            }
        }

        if (!Objects.equals(items.size(), 0)) {

            // Insert order into the database
            orderRepository.save(order);

            // Insert items into the database
            itemRepository.saveAll(items);

            // Update inventory
            for (Item item : items) {

                // Select inventory item from the database
                Inventory inventoryItem = inventoryRepository.findById(item.getInventory_item().getId())
                        .orElseThrow(() -> {
                            String message = "Item with id " +
                                    item.getInventory_item().getId() +
                                    " does not exist in inventory!";
                            return new IllegalStateException(message);
                        });

                // Update inventory item's quantity
                if (item.getSelected_quantity() != null
                        && !Objects.equals(inventoryItem.getQuantity(),
                        inventoryItem.getQuantity() - item.getSelected_quantity())) {
                    inventoryItem.setQuantity(inventoryItem.getQuantity() - item.getSelected_quantity());
                }
            }
        }
    }

    @Transactional
    @Override
    public void updateOrder(Long id, Long table_id, String status) {

        // Select order from the database
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Order with id " + id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Update order's table id
        if (table_id != null && !Objects.equals(order.getTable().getId(), table_id)) {

            TableEntity table = tableRepository.findById(table_id)
                    .orElseThrow(() -> {
                        String message = "Table with id " + table_id + " does not exist!";
                        return new IllegalStateException(message);
                    });

            order.setTable(table);
        }

        // Update order's availability
        if (status != null && !Objects.equals(order.getStatus(), getStatusFromRequest(status))) {
            order.setStatus(getStatusFromRequest(status));
        }
    }

    @Override
    public void deleteOrder(Long id) {

        // True if order exists, False otherwise
        boolean exists = orderRepository.existsById(id);

        // If order does not exist, throw exception
        if (!exists) {
            String message = "Order with id " + id + " does not exist!";
            throw new IllegalStateException(message);
        }

        // Delete order from the database
        orderRepository.deleteById(id);
    }
}
