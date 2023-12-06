package com.unipi.pfatouros.eAssist_backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unipi.pfatouros.eAssist_backend.entity.TableEntity;
import com.unipi.pfatouros.eAssist_backend.service.implementation.TableServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/tables")
@RequiredArgsConstructor
public class TableController {

    // Use the corresponding service implementation to process incoming API requests
    private final TableServiceImpl tableService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public List<TableEntity> getTables() {

        return tableService.getTables();
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public void addTable(@RequestBody ObjectNode table) {

        tableService.addTable(table.get("name").asText(), table.get("is_available").asBoolean());
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public void updateTable(@PathVariable("id") Long id,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) Boolean is_available) {

        tableService.updateTable(id, name, is_available);
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteTable(@PathVariable("id") Long id) {

        tableService.deleteTable(id);
    }
}
