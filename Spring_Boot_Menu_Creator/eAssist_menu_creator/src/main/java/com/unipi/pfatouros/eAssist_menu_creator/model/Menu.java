package com.unipi.pfatouros.eAssist_menu_creator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Menu {

    private List<Category> categories;

    private List<Item> items;
}
