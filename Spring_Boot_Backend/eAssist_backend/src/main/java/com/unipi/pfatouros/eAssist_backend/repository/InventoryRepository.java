package com.unipi.pfatouros.eAssist_backend.repository;

import com.unipi.pfatouros.eAssist_backend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Contains the basic CRUD operations for Inventory entity
}
