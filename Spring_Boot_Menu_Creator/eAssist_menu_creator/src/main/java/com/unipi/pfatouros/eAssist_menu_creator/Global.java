package com.unipi.pfatouros.eAssist_menu_creator;

import com.unipi.pfatouros.eAssist_menu_creator.model.Category;
import com.unipi.pfatouros.eAssist_menu_creator.model.Item;
import com.unipi.pfatouros.eAssist_menu_creator.model.Menu;

import java.util.List;

public class Global {

    // Contains the project's global variables

    public static List<Category> categories;

    public static List<Item> items;

    public static Menu menu;

    public static final String FILE_NAME = System.getProperty("user.home") + "/Downloads/menu.json";

    public static final String FILE_URL = "http://localhost:8081/menu/json";
}
