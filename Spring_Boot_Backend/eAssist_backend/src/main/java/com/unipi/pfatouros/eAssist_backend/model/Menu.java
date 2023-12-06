package com.unipi.pfatouros.eAssist_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Menu {

    private List<Category> categories;

    private List<InventoryItem> items;
}
