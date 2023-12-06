package com.unipi.pfatouros.eAssist_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unipi.pfatouros.eAssist_backend.entity.*;
import com.unipi.pfatouros.eAssist_backend.model.InventoryItem;
import com.unipi.pfatouros.eAssist_backend.model.Menu;
import com.unipi.pfatouros.eAssist_backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
public class DatabaseConfig {

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository,
                                        PasswordEncoder encoder,
                                        UserRepository userRepository,
                                        TableRepository tableRepository,
                                        InventoryRepository inventoryRepository,
                                        OrderRepository orderRepository,
                                        ItemRepository itemRepository) {
        return args -> {

            // Create roles
            Role employee = new Role(RoleEnum.ROLE_EMPLOYEE);
            Role manager = new Role(RoleEnum.ROLE_MANAGER);
            Role admin = new Role(RoleEnum.ROLE_ADMIN);

            // Save roles to database
            roleRepository.saveAll(List.of(employee, manager, admin));

            // Create users
            User peter = new User(
                    "peter",
                    "peter@peter.com",
                    encoder.encode("123456"),
                    new HashSet<>(List.of(admin))
            );
            User jim = new User(
                    "jim",
                    "jim@jim.com",
                    encoder.encode("123456"),
                    new HashSet<>(List.of(manager))
            );
            User athena = new User(
                    "athena",
                    "athena@athena.com",
                    encoder.encode("123456"),
                    new HashSet<>(List.of(employee))
            );
            User timmy = new User(
                    "timmy",
                    "timmy@timmy.com",
                    encoder.encode("123456"),
                    new HashSet<>(List.of(employee, manager))
            );

            // Save users to database
            userRepository.saveAll(List.of(peter, jim, athena, timmy));

            // Create tables
            TableEntity beach1 = new TableEntity("beach1", true);
            TableEntity beach2 = new TableEntity("beach2", true);
            TableEntity beach3 = new TableEntity("beach3", true);
            TableEntity beach4 = new TableEntity("beach4", true);
            TableEntity beach5 = new TableEntity("beach5", true);
            TableEntity bar1 = new TableEntity("bar1", true);
            TableEntity bar2 = new TableEntity("bar2", true);
            TableEntity bar3 = new TableEntity("bar3", true);
            TableEntity bar4 = new TableEntity("bar4", true);
            TableEntity bar5 = new TableEntity("bar5", true);

            // Save tables to database
            tableRepository.saveAll(List.of(beach1, beach2, beach3, beach4, beach5, bar1, bar2, bar3, bar4, bar5));

            // Load menu from file
            Menu menu = new ObjectMapper().readValue(Paths.get("menu.json").toFile(), Menu.class);

            // Create inventory
            List<Inventory> inventory = new ArrayList<>();
            for (InventoryItem item : menu.getItems()) {
                inventory.add(new Inventory(
                        item.getName(),
                        item.getCategory().getName(),
                        item.getPrice(),
                        item.getQuantity()
                ));
            }

            // Save inventory to database
            inventoryRepository.saveAll(inventory);

            // Create orders
            Order order1 = new Order(beach1, OrderEnum.ACTIVE, Date.valueOf("2022-09-01"));
            Order order2 = new Order(beach2, OrderEnum.READY, Date.valueOf("2022-09-01"));
            Order order3 = new Order(beach3, OrderEnum.UNPAID, Date.valueOf("2022-09-01"));
            Order order4 = new Order(beach4, OrderEnum.COMPLETE, Date.valueOf("2022-09-01"));
            Order order5 = new Order(bar1, OrderEnum.ACTIVE, new Date(System.currentTimeMillis()));
            Order order6 = new Order(bar2, OrderEnum.READY, new Date(System.currentTimeMillis()));
            Order order7 = new Order(bar3, OrderEnum.UNPAID, new Date(System.currentTimeMillis()));
            Order order8 = new Order(bar4, OrderEnum.COMPLETE, new Date(System.currentTimeMillis()));

            // Save orders to database
            orderRepository.saveAll(List.of(order1, order2, order3, order4, order5, order6, order7, order8));

            // Create items
            int index = 0;
            Item item1 = new Item(order1, inventory.get(index++), index);
            Item item2 = new Item(order1, inventory.get(index++), index);
            Item item3 = new Item(order2, inventory.get(index++), index);
            Item item4 = new Item(order2, inventory.get(index++), index);
            Item item5 = new Item(order3, inventory.get(index++), index);
            Item item6 = new Item(order3, inventory.get(index++), index);
            Item item7 = new Item(order4, inventory.get(index++), index);
            Item item8 = new Item(order4, inventory.get(index++), index);
            Item item9 = new Item(order5, inventory.get(index++), index);
            Item item10 = new Item(order5, inventory.get(index++), index);
            Item item11 = new Item(order6, inventory.get(index++), index);
            Item item12 = new Item(order6, inventory.get(index++), index);
            Item item13 = new Item(order7, inventory.get(index++), index);
            Item item14 = new Item(order7, inventory.get(index++), index);
            Item item15 = new Item(order8, inventory.get(index++), index);
            Item item16 = new Item(order8, inventory.get(index++), index);

            // Save items to database
            itemRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10,
                    item11, item12, item13, item14, item15, item16));
        };
    }
}
