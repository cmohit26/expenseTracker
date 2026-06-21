package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.Services.DashboardService;
import com.mohit.expense_backend.dto.DashboardDTOs.DashboardDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboardData();
    }
}