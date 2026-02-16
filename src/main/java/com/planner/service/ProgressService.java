package com.planner.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planner.model.Progress;
import com.planner.model.Todo;
import com.planner.model.User;
import com.planner.model.enums.TodoStatus;
import com.planner.repository.ProgressRepository;
import com.planner.repository.TodoRepository;
import com.planner.repository.UserRepository;

@Service
public class ProgressService {
    
    @Autowired
    private ProgressRepository progressRepository;
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public void calculateProgressForDay(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Todo> todos = todoRepository.findByUserIdAndDate(userId, date);
        
        int total = todos.size();
        long completed = todos.stream()
            .filter(t -> t.getStatus() == TodoStatus.DONE)
            .count();
        
        double percentage = total > 0 ? (completed * 100.0 / total) : 0;
        
        Optional<Progress> existingProgress = progressRepository.findByUserIdAndDate(userId, date);
        
        Progress progress = existingProgress.orElse(new Progress());
        progress.setUser(user);
        progress.setDate(date);
        progress.setTotalTasks(total);
        progress.setCompletedTasks((int) completed);
        progress.setCompletionPercentage(percentage);
        
        progressRepository.save(progress);
    }
    
    public List<Progress> getMonthlyProgress(Long userId, LocalDate startDate, LocalDate endDate) {
        return progressRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }
    
    public Progress getProgressForDay(Long userId, LocalDate date) {
        Optional<Progress> progress = progressRepository.findByUserIdAndDate(userId, date);
        return progress.orElse(null);
    }
    
    public double getMonthlyAverageCompletion(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Progress> progressList = getMonthlyProgress(userId, startDate, endDate);
        
        if (progressList.isEmpty()) {
            return 0;
        }
        
        double average = progressList.stream()
            .mapToDouble(Progress::getCompletionPercentage)
            .average()
            .orElse(0);
        
        return Math.round(average * 100.0) / 100.0;
    }
}