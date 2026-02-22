package com.planner.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planner.dto.TimetableDTO;
import com.planner.dto.TimetableRequest;
import com.planner.model.Timetable;
import com.planner.service.TimetableService;

import com.planner.model.User;
import com.planner.repository.UserRepository;
import com.planner.service.UserService;

@RestController
@RequestMapping("/api/timetable")
@PreAuthorize("isAuthenticated()")
public class TimetableController {
    
    @Autowired
    private TimetableService timetableService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<TimetableDTO>> getTimetable(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Timetable> timetables = timetableService.getTimetableByDate(userId, date);
        List<TimetableDTO> dtos = timetables.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/range")
    public ResponseEntity<List<TimetableDTO>> getTimetableByRange(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Timetable> timetables = timetableService.getTimetableByDateRange(userId, startDate, endDate);
        List<TimetableDTO> dtos = timetables.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping
    public ResponseEntity<TimetableDTO> createTimetable(
            @RequestBody TimetableRequest request) {

        Timetable timetable = timetableService.createTimetable(
                request.getUserId(),
                request.getDate(),
                request.getActivity(),
                request.getStartTime(),
                request.getEndTime()
        );

        // ✅ UPDATE STREAK
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user != null) {
            userService.updateStreak(user);
            userRepository.save(user);
        }

        return new ResponseEntity<>(convertToDTO(timetable), HttpStatus.CREATED);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearTimetable(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        timetableService.clearTimetableForDate(userId, date);
        return ResponseEntity.noContent().build();
    }
    
    private TimetableDTO convertToDTO(Timetable timetable) {
        TimetableDTO dto = new TimetableDTO();
        dto.setId(timetable.getId());
        dto.setActivity(timetable.getActivity());
        dto.setDate(timetable.getDate());
        dto.setStartTime(timetable.getStartTime());
        dto.setEndTime(timetable.getEndTime());
        return dto;
    }
}