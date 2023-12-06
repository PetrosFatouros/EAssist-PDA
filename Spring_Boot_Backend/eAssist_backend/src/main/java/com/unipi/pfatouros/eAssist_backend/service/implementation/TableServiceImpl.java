package com.unipi.pfatouros.eAssist_backend.service.implementation;

import com.unipi.pfatouros.eAssist_backend.entity.TableEntity;
import com.unipi.pfatouros.eAssist_backend.repository.TableRepository;
import com.unipi.pfatouros.eAssist_backend.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    // Implementation of methods which contain the business logic related to the TableEntity entity

    private final TableRepository tableRepository;

    @Override
    public List<TableEntity> getTables() {

        // Select all tables from the database
        return tableRepository.findAll();
    }

    @Override
    public void addTable(String name, Boolean is_available) {

        // Check if another table with the same name already exists in the database
        boolean existsByName = tableRepository.existsByName(name);
        if (existsByName) {
            String message = "Error: Table with name " + name + " already exists!";
            throw new IllegalStateException(message);
        }

        // Create new table
        TableEntity table = new TableEntity(name, is_available);

        // Insert table into the database
        tableRepository.save(table);
    }

    @Transactional
    @Override
    public void updateTable(Long id, String name, Boolean is_available) {

        // Select table from the database
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Table with id " + id + " does not exist!";
                    return new IllegalStateException(message);
                });

        // Update table's name
        if (name != null && name.length() > 0 && !Objects.equals(table.getName(), name)) {
            Optional<TableEntity> tableOptional = tableRepository.findByName(name);

            if (tableOptional.isPresent()) {
                String message = "Table with name " + name + " already exists!";
                throw new IllegalStateException(message);
            }

            table.setName(name);
        }

        // Update table's availability
        if (is_available != null && !Objects.equals(table.getIs_available(), is_available)) {
            table.setIs_available(is_available);
        }
    }

    @Override
    public void deleteTable(Long id) {

        // True if table exists, False otherwise
        boolean exists = tableRepository.existsById(id);

        // If table does not exist, throw exception
        if (!exists) {
            String message = "Table with id " + id + " does not exist!";
            throw new IllegalStateException(message);
        }

        // Delete table from the database
        tableRepository.deleteById(id);
    }
}
