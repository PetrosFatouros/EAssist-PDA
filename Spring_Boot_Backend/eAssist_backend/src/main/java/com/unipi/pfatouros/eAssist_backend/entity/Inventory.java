package com.unipi.pfatouros.eAssist_backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Inventory {

    // This class corresponds to inventory items

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @Column(length = 50, nullable = false, unique = true)
    private String item_name; // Name of the item

    @Column(length = 20, nullable = false)
    private String category; // Category of the item (e.g. drink, food, etc)

    @Column(nullable = false)
    private Float price; // Price of the item

    @Column(nullable = false)
    private Integer quantity; // Item stock

    // Constructor
    public Inventory(String item_name, String category, Float price, Integer quantity) {
        this.item_name = item_name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}
