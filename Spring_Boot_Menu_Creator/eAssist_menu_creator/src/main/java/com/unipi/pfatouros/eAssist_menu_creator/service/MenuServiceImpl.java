package com.unipi.pfatouros.eAssist_menu_creator.service;

import com.unipi.pfatouros.eAssist_menu_creator.Global;
import com.unipi.pfatouros.eAssist_menu_creator.model.Category;
import com.unipi.pfatouros.eAssist_menu_creator.model.Item;
import com.unipi.pfatouros.eAssist_menu_creator.model.Menu;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Service
public class MenuServiceImpl implements MenuService {

    // Implementation of methods which contain the business logic related to the Menu model

    @Override
    public Boolean addCategory(Category category) {

        // Check if category exists
        boolean exists = false;
        for(Category _category: Global.categories){
            if (_category.getName().equals(category.getName())) {
                exists = true;
                break;
            }
        }

        // If category does not already exist, add it to set
        if(exists){
            return false;
        }
        else {
            Global.categories.add(category);
            return true;
        }
    }

    @Override
    public Boolean addItem(Item item) {

        // Check if item exists
        boolean exists = false;
        for(Item _item: Global.items){
            if (_item.getName().equals(item.getName())) {
                exists = true;
                break;
            }
        }

        // If item does not already exist, add it to set
        if(exists){
            return false;
        }
        else {
            Global.items.add(item);
            return true;
        }
    }

    @Override
    public void deleteCategory(String category_name) {

        // Check if category exists
        Category category = null;
        for(Category _category: Global.categories){
            if (_category.getName().equals(category_name)) {
                category = _category;
                break;
            }
        }

        // If category does exist, remove it from set
        if(category != null){

            Global.categories.remove(category);
        }
    }

    @Override
    public void deleteItem(String item_name) {

        // Check if item exists
        Item item = null;
        for(Item _item: Global.items){
            if (_item.getName().equals(item_name)) {
                item = _item;
                break;
            }
        }

        // If item does exist, remove it from set
        if(item != null){

            Global.items.remove(item);
        }
    }

    @Override
    public void downloadMenu() throws IOException {

        // Create menu
        Global.menu = new Menu(Global.categories, Global.items);

        // Download menu
        URL url = new URL(Global.FILE_URL);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        @SuppressWarnings("resource") FileOutputStream fos = new FileOutputStream(Global.FILE_NAME);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
