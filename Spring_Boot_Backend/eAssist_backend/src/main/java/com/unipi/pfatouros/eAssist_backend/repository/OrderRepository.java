package com.unipi.pfatouros.eAssist_backend.repository;

import com.unipi.pfatouros.eAssist_backend.entity.Order;
import com.unipi.pfatouros.eAssist_backend.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Contains the basic CRUD operations for Order entity

    List<Order> findOrdersByTable(TableEntity table);

    List<Order> findOrdersByDate(Date date);

    List<Order> findOrdersByTableAndDate(TableEntity table, Date date);
}
