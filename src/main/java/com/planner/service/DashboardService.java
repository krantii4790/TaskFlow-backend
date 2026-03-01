package com.planner.service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.planner.model.Progress;
import com.planner.model.Todo;
import com.planner.model.enums.TodoStatus;
import com.planner.repository.ProgressRepository;
import com.planner.repository.TodoRepository;
@Service
public class DashboardService {
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private ProgressRepository progressRepository;
    
    public Map<String, Object> getDashboardSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        
        List<Todo> todaysTodos = todoRepository.findByUserIdAndDate(userId, today);
        int totalTasks = todaysTodos.size();
        int completedTasks = (int) todaysTodos.stream()
            .filter(t -> t.getStatus() == TodoStatus.DONE)
            .count();
        
        double completionPercentage = totalTasks > 0 ? (completedTasks * 100.0 / totalTasks) : 0;
        
        summary.put("totalTasks", totalTasks);
        summary.put("completedTasks", completedTasks);
        summary.put("completionPercentage", Math.round(completionPercentage * 100.0) / 100.0);
        summary.put("date", today);
        
        return summary;
    }
    
    public Map<String, Object> getWeeklyStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);
        
        List<Progress> weeklyProgress = progressRepository.findByUserIdAndDateBetween(userId, weekStart, weekEnd);
        
        stats.put("weekStart", weekStart);
        stats.put("weekEnd", weekEnd);
        stats.put("dailyProgress", weeklyProgress);
        
        double averageCompletion = weeklyProgress.stream()
            .mapToDouble(Progress::getCompletionPercentage)
            .average()
            .orElse(0);
        
        stats.put("weeklyAverage", Math.round(averageCompletion * 100.0) / 100.0);
        
        return stats;
    }
}