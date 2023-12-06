package com.unipi.pfatouros.eAssist_menu_creator.service;

import com.unipi.pfatouros.eAssist_menu_creator.model.Category;
import com.unipi.pfatouros.eAssist_menu_creator.model.Item;

import java.io.IOException;

public interface MenuService {

    // Declaration of methods which contain the business logic related to the Menu model

    Boolean addCategory(Category category);

    Boolean addItem(Item item);

    void deleteCategory(String category_name);

    void deleteItem(String item_name);

    void downloadMenu() throws IOException;
}
