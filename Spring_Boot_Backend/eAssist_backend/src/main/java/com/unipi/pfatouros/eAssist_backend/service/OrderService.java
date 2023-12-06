package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.Order;
import com.unipi.pfatouros.eAssist_backend.entity.OrderEnum;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface OrderService {

    // Declaration of methods which contain the business logic related to the Order entity

    // Get the status of an order (convert String to OrderEnum)
    OrderEnum getStatusFromRequest(@NotBlank String status);

    // Get a list that contains all orders from all tables regardless of status
    List<Order> getOrders();

    // Get a list that contains all the orders of an existing table
    List<Order> getOrders(@NotNull Long table_id);

    // Get a list that contains all the orders that have a specific status
    List<Order> getOrdersBasedOnStatus(@NotBlank String status);

    // Get a list that contains all orders of a given date
    List<Order> getOrdersBasedOnDate(@NotBlank String date);

    // Get a list that contains all orders of an existing table of a given date
    List<Order> getOrders(@NotNull Long table_id, @NotBlank String date);

    // Get a list that contains all the orders of a given date that have a specific status
    List<Order> getOrders(@NotBlank String status, @NotBlank String date);

    // Create a new order
    void addOrder(@NotNull Long table_id);

    // Create a new order and associate it with given items
    void addFullOrder(@NotNull Long table_id,
                      @NotEmpty List<Long> inventoryIds,
                      @NotEmpty List<Integer> selectedQuantities);

    // Update an existing order
    void updateOrder(@NotNull Long order_id, Long table_id, String status);

    // Delete an existing order
    void deleteOrder(@NotNull Long order_id);
}
