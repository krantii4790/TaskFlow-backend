package com.planner.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planner.dto.ProgressDTO;
import com.planner.model.Progress;
import com.planner.service.ProgressService;

@RestController
@RequestMapping("/api/progress")
@PreAuthorize("isAuthenticated()")
public class ProgressController {
    
    @Autowired
    private ProgressService progressService;
    
    @GetMapping
    public ResponseEntity<ProgressDTO> getProgress(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        Progress progress = progressService.getProgressForDay(userId, date);
        return ResponseEntity.ok(convertToDTO(progress));
    }
    
    @GetMapping("/monthly")
    public ResponseEntity<List<ProgressDTO>> getMonthlyProgress(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Progress> progressList = progressService.getMonthlyProgress(userId, startDate, endDate);
        List<ProgressDTO> dtos = progressList.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping("/calculate")
    public ResponseEntity<ProgressDTO> calculateProgress(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        progressService.calculateProgressForDay(userId, date);
        Progress progress = progressService.getProgressForDay(userId, date);
        
        return ResponseEntity.ok(convertToDTO(progress));
    }
    
    private ProgressDTO convertToDTO(Progress progress) {
        if (progress == null) {
            return new ProgressDTO(null, 0, 0, 0.0);
        }
        return new ProgressDTO(
            progress.getDate(),
            progress.getTotalTasks(),
            progress.getCompletedTasks(),
            progress.getCompletionPercentage()
        );
    }
}