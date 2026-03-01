package com.planner.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planner.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@RequestParam Long userId) {
        Map<String, Object> summary = dashboardService.getDashboardSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> getWeeklyStats(@RequestParam Long userId) {
        Map<String, Object> stats = dashboardService.getWeeklyStats(userId);
        return ResponseEntity.ok(stats);
    }

    // ✅ ADDED: Missing monthly endpoint that frontend now calls
    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyStats(@RequestParam Long userId) {
        Map<String, Object> stats = dashboardService.getMonthlyStats(userId);
        return ResponseEntity.ok(stats);
    }
}