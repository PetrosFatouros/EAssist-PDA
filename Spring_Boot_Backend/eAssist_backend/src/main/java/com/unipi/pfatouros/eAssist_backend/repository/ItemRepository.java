package com.unipi.pfatouros.eAssist_backend.repository;

import com.unipi.pfatouros.eAssist_backend.entity.Item;
import com.unipi.pfatouros.eAssist_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Contains the basic CRUD operations for Item entity

    List<Item> findItemsByOrder(Order order);
}
