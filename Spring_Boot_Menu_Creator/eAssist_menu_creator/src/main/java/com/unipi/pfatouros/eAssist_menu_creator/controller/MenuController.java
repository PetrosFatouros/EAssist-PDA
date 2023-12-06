package com.unipi.pfatouros.eAssist_menu_creator.controller;

import com.unipi.pfatouros.eAssist_menu_creator.Global;
import com.unipi.pfatouros.eAssist_menu_creator.model.Category;
import com.unipi.pfatouros.eAssist_menu_creator.model.Item;
import com.unipi.pfatouros.eAssist_menu_creator.service.MenuServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuServiceImpl menuService;

    // POST: add category
    @PostMapping(path = "/category")
    public String category(@Valid @ModelAttribute("category") Category category){

        // Initialize variable
        if(Global.categories == null){
            Global.categories = new ArrayList<>();
        }

        if (menuService.addCategory(category)) {
            return "redirect:/category?success";
        }
        else{
            return "redirect:/category?error";
        }
    }

    // POST: add item
    @PostMapping(path = "/item")
    public String item(@Valid @ModelAttribute("item") Item item){

        // Initialize variable
        if(Global.items == null){
            Global.items = new ArrayList<>();
        }

        if (menuService.addItem(item)) {
            return "redirect:/item?success";
        }
        else{
            return "redirect:/item?error";
        }
    }

    // Delete category
    @GetMapping(path = "/category/{category_name}")
    public String deleteCategory(@PathVariable("category_name") String category_name){

        // Initialize variable
        if(Global.categories == null){
            Global.categories = new ArrayList<>();
        }

        // Delete category
        menuService.deleteCategory(category_name);

        return "redirect:/menu";
    }

    // Delete item
    @GetMapping(path = "/item/{item_name}")
    public String deleteItem(@PathVariable("item_name") String item_name){

        // Initialize variable
        if(Global.items == null){
            Global.items = new ArrayList<>();
        }

        // Delete item
        menuService.deleteItem(item_name);

        return "redirect:/menu";
    }

    // Download menu
    @GetMapping(path = "/menu/download")
    public String downloadMenu() throws IOException {

        // Download menu
        menuService.downloadMenu();

        return "redirect:/home?success";
    }
}
