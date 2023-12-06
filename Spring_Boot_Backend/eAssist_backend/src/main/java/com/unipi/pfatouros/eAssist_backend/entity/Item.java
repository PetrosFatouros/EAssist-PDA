package com.unipi.pfatouros.eAssist_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "items")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("JpaDataSourceORMInspection")
public class Item {

    // This class corresponds to the items of an order

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order; // Associated order

    @OneToOne
    @JoinColumn(name = "inventory_item_id", referencedColumnName = "id")
    private Inventory inventory_item; // Associated inventory item

    @Column(nullable = false)
    private Integer selected_quantity; // Times the item is selected for current order

    // Constructor
    public Item(Order order, Inventory inventory_item, Integer selected_quantity) {
        this.order = order;
        this.inventory_item = inventory_item;
        this.selected_quantity = selected_quantity;
    }
}
