package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.entity.Role;
import com.unipi.pfatouros.eAssist_backend.service.implementation.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/roles")
@RequiredArgsConstructor
public class RoleController {

    // Use the corresponding service implementation to process incoming API requests
    private final RoleServiceImpl roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Role> getRoles() {

        return roleService.getRoles();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    void addRole(@RequestBody ObjectNode name) {

        roleService.addRole(name.get("name").asText());
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(@PathVariable("id") Long id) {

        roleService.deleteRole(id);
    }
}
