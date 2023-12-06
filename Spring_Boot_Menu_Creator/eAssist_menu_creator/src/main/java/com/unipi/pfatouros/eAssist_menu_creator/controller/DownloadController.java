package com.unipi.pfatouros.eAssist_menu_creator.controller;

import com.unipi.pfatouros.eAssist_menu_creator.Global;
import com.unipi.pfatouros.eAssist_menu_creator.model.Menu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadController {

    // GET: current menu in JSON format
    @GetMapping(path = "/menu/json")
    public Menu downloadMenu(){

        return Global.menu;
    }
}
