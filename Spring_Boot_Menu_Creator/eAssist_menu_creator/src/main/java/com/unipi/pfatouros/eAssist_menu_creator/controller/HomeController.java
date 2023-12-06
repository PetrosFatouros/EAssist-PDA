package com.unipi.pfatouros.eAssist_menu_creator.controller;

import com.unipi.pfatouros.eAssist_menu_creator.Global;
import com.unipi.pfatouros.eAssist_menu_creator.model.Category;
import com.unipi.pfatouros.eAssist_menu_creator.model.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class HomeController {

    // GET: index.html
    @GetMapping
    public String index() {
        return "index";
    }

    // GET: index.html
    @GetMapping(path = "/home")
    public String home() {
        return "index";
    }

    // GET: category.html
    @GetMapping(path = "/category")
    public String category(Model model) {

        // Add attribute to model
        model.addAttribute("category", new Category());

        return "category";
    }

    // GET: item.html
    @GetMapping(path = "/item")
    public String item(Model model) {

        // Initialize variable
        if(Global.categories == null){
            Global.categories = new ArrayList<>();
        }

        // Add attribute to model
        model.addAttribute("categories", Global.categories);

        // Add attribute to model
        model.addAttribute("item", new Item());

        return "item";
    }

    // GET: menu.html
    @GetMapping(path = "/menu")
    public String menu(Model model) {

        // Initialize variables
        if(Global.categories == null){
            Global.categories = new ArrayList<>();
        }
        if(Global.items == null){
            Global.items = new ArrayList<>();
        }

        // Add attribute to model
        model.addAttribute("categories", Global.categories);

        // Add attribute to model
        model.addAttribute("items", Global.items);

        return "menu";
    }
}
