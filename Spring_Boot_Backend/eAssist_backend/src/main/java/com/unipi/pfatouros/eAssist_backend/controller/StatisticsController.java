package com.unipi.pfatouros.eAssist_backend.controller;

import com.unipi.pfatouros.eAssist_backend.model.ItemStats;
import com.unipi.pfatouros.eAssist_backend.service.implementation.StatisticsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    // Use the corresponding service implementation to process incoming API requests
    private final StatisticsServiceImpl statisticsService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<ItemStats> getStatistics() {

        return statisticsService.getStatistics();
    }

    @GetMapping(path = "/{date}")
    @PreAuthorize("hasRole('MANAGER')")
    public List<ItemStats> getStatistics(@PathVariable("date") String date) {

        return statisticsService.getStatistics(date);
    }
}
