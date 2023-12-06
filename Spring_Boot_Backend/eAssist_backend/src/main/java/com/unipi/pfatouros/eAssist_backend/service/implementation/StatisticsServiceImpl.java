package com.unipi.pfatouros.eAssist_backend.service.implementation;

import com.unipi.pfatouros.eAssist_backend.entity.Item;
import com.unipi.pfatouros.eAssist_backend.entity.Order;
import com.unipi.pfatouros.eAssist_backend.model.ItemStats;
import com.unipi.pfatouros.eAssist_backend.repository.ItemRepository;
import com.unipi.pfatouros.eAssist_backend.repository.OrderRepository;
import com.unipi.pfatouros.eAssist_backend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    // Implementation of methods which contain the business logic related to the ItemStats model

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemStats> getStatistics() {

        // Select all orders
        List<Order> orders = orderRepository.findAll();

        // Return statistics
        return getStatistics(orders);
    }

    @Override
    public List<ItemStats> getStatistics(String date) {

        // Select all orders of given date
        List<Order> orders = orderRepository.findOrdersByDate(Date.valueOf(date));

        // Return statistics
        return getStatistics(orders);
    }

    @Override
    public List<ItemStats> getStatistics(List<Order> orders) {

        // Get statistics
        List<ItemStats> statistics = new ArrayList<>();
        for (Order order : orders) {

            List<Item> items = itemRepository.findItemsByOrder(order);
            for (Item item : items) {

                ItemStats itemStats = new ItemStats(
                        item.getInventory_item(),
                        item.getSelected_quantity(),
                        item.getInventory_item().getPrice() * item.getSelected_quantity());

                boolean exists = false;
                for (ItemStats _itemStats : statistics) {
                    if (Objects.equals(itemStats.getInventory_item().getId(), _itemStats.getInventory_item().getId())) {
                        exists = true;
                        itemStats = _itemStats;
                        break;
                    }
                }

                if (exists) {

                    int position = statistics.indexOf(itemStats);
                    ItemStats _itemStats = statistics.get(position);

                    Integer current_times_sold = _itemStats.getTimes_sold();
                    _itemStats.setTimes_sold(current_times_sold + item.getSelected_quantity());

                    Float current_profit = _itemStats.getProfit();
                    _itemStats.setProfit(current_profit
                            + item.getInventory_item().getPrice() * item.getSelected_quantity());
                } else {
                    statistics.add(itemStats);
                }
            }
        }

        // Return statistics
        return statistics;
    }
}
