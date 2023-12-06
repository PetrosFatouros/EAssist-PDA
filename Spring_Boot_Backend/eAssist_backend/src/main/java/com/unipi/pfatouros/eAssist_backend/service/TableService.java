package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.TableEntity;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface TableService {

    // Declaration of methods which contain the business logic related to the Table entity

    // Get a list that contains all existing tables
    List<TableEntity> getTables();

    // Add a new table
    void addTable(@NotBlank String name, @NotNull Boolean is_available);

    // Update existing table
    void updateTable(@NotNull Long id, String name, Boolean is_available);

    // Delete existing table
    void deleteTable(@NotNull Long id);
}
