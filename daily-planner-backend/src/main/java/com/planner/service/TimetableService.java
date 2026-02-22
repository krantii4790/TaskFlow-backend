package com.planner.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planner.model.Timetable;
import com.planner.model.User;
import com.planner.repository.TimetableRepository;
import com.planner.repository.UserRepository;

@Service
public class TimetableService {
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Timetable> getTimetableByDate(Long userId, LocalDate date) {
        return timetableRepository.findByUserIdAndDate(userId, date);
    }
    
    public List<Timetable> getTimetableByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return timetableRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }
    
    

    public Timetable createTimetable(
            Long userId,
            LocalDate date,
            String activity,
            String startTime,
            String endTime) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Timetable timetable = new Timetable();
        timetable.setUser(user);
        timetable.setDate(date);
        timetable.setActivity(activity);

        // ✅ FIX: convert String → LocalTime
        timetable.setStartTime(LocalTime.parse(startTime));
        timetable.setEndTime(LocalTime.parse(endTime));

        return timetableRepository.save(timetable);
    }

    
    public void deleteTimetable(Long id) {
        timetableRepository.deleteById(id);
    }
    
    public void clearTimetableForDate(Long userId, LocalDate date) {
        timetableRepository.deleteByUserIdAndDate(userId, date);
    }
}
