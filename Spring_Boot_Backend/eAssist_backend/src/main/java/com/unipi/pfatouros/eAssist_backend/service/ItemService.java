package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.Item;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface ItemService {

    // Declaration of methods which contain the business logic related to the Item entity

    // Get a list that contains all items of given order
    List<Item> getItems(@NotNull Long order_id);

    // Add item to an existing order
    void addItem(@NotNull Long order_id, @NotNull Long inventory_item_id, @NotNull Integer selected_quantity);

    // Update an existing item
    void updateItem(@NotNull Long id, Long order_id, Long inventory_item_id);

    // Delete and remove an item from an existing order
    void deleteItem(@NotNull Long id);
}
