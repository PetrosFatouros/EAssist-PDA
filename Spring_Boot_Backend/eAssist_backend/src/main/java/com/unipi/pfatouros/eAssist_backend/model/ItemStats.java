package com.unipi.pfatouros.eAssist_backend.model;

import com.unipi.pfatouros.eAssist_backend.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ItemStats {

    private Inventory inventory_item; // Associated inventory item

    private Integer times_sold; // Times the item was sold

    private Float profit; // Total profit the item made
}
