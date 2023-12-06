package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.service.implementation.RoleServiceImpl;
import com.unipi.pfatouros.eAssist_backend.service.implementation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Use the corresponding service implementation to process incoming API requests
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @PostMapping(path = "/sign_in")
    public String authenticateUser(@RequestBody ObjectNode request) {

        return userService.authenticateUser(request.get("username").asText(), request.get("password").asText());
    }

    @GetMapping(path = "/employee/{username}")
    public Boolean hasRoleEmployee(@PathVariable("username") String username) {

        return roleService.hasRoleEmployee(username);
    }

    @GetMapping(path = "/manager/{username}")
    public Boolean hasRoleManager(@PathVariable("username") String username) {

        return roleService.hasRoleManager(username);
    }

    @GetMapping(path = "/admin/{username}")
    public Boolean hasRoleAdmin(@PathVariable("username") String username) {

        return roleService.hasRoleAdmin(username);
    }

}
