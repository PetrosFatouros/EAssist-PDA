package com.unipi.pfatouros.eAssist_backend.service;

import com.unipi.pfatouros.eAssist_backend.entity.Order;
import com.unipi.pfatouros.eAssist_backend.model.ItemStats;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface StatisticsService {

    // Declaration of methods which contain the business logic related to the Item entity

    // Get statistics based on all orders (regardless of date)
    List<ItemStats> getStatistics();

    // Get statistics based on orders of specific date
    List<ItemStats> getStatistics(@NotBlank String date);

    // Process orders and gather statistics
    List<ItemStats> getStatistics(@NotNull List<Order> orders);
}
