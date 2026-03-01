package com.planner.service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    // ✅ FIXED: Added streak calculation — was missing before, always returned 0
    public Map<String, Object> getDashboardSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();

        LocalDate today = LocalDate.now();

        List<Todo> todaysTodos = todoRepository.findByUserIdAndDate(userId, today);
        int totalTasks = todaysTodos.size();
        int completedTasks = (int) todaysTodos.stream()
                .filter(t -> t.getStatus() == TodoStatus.DONE)
                .count();

        double completionPercentage = totalTasks > 0
                ? (completedTasks * 100.0 / totalTasks)
                : 0;

        // ✅ FIXED: Streak — count today if fully complete, then walk back through past days
        int streak = 0;

        if (totalTasks > 0 && completedTasks == totalTasks) {
            streak++; // today counts
        }

        LocalDate checkDate = today.minusDays(1);
        for (int i = 0; i < 365; i++) {
            List<Todo> dayTodos = todoRepository.findByUserIdAndDate(userId, checkDate);
            if (dayTodos.isEmpty()) break; // gap in streak, stop

            long done = dayTodos.stream()
                    .filter(t -> t.getStatus() == TodoStatus.DONE)
                    .count();

            if (done == dayTodos.size()) {
                streak++;
                checkDate = checkDate.minusDays(1);
            } else {
                break; // incomplete day breaks the streak
            }
        }

        summary.put("totalTasks", totalTasks);
        summary.put("completedTasks", completedTasks);
        summary.put("completionPercentage", Math.round(completionPercentage * 100.0) / 100.0);
        summary.put("streak", streak); // ✅ FIXED: now actually included in response
        summary.put("date", today);

        return summary;
    }

    // ✅ UNCHANGED: This was already correct — no changes needed
    public Map<String, Object> getWeeklyStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Progress> weeklyProgress = progressRepository.findByUserIdAndDateBetween(
                userId, weekStart, weekEnd);

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

    // ✅ NEW: Was completely missing — needed by the new /monthly endpoint
    public Map<String, Object> getMonthlyStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());

        List<Map<String, Object>> weeklyBreakdown = new ArrayList<>();

        LocalDate weekStart = monthStart;
        int weekNum = 1;

        // Split the current month into up to 4 weekly chunks
        while (!weekStart.isAfter(monthEnd) && weekNum <= 4) {
            LocalDate weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(monthEnd)) {
                weekEnd = monthEnd; // don't go past end of month
            }

            // Uses existing findByUserIdAndDateBetween from your TodoRepository ✅
            List<Todo> weekTodos = todoRepository.findByUserIdAndDateBetween(
                    userId, weekStart, weekEnd);

            int totalTasks = weekTodos.size();
            int completedTasks = (int) weekTodos.stream()
                    .filter(t -> t.getStatus() == TodoStatus.DONE)
                    .count();

            double weekCompletion = totalTasks > 0
                    ? (completedTasks * 100.0 / totalTasks)
                    : 0;

            Map<String, Object> weekData = new HashMap<>();
            weekData.put("week", "Week " + weekNum);
            weekData.put("totalTasks", totalTasks);
            weekData.put("completedTasks", completedTasks);
            weekData.put("completionPercentage",
                    Math.round(weekCompletion * 100.0) / 100.0);

            weeklyBreakdown.add(weekData);
            weekStart = weekEnd.plusDays(1);
            weekNum++;
        }

        stats.put("monthStart", monthStart);
        stats.put("monthEnd", monthEnd);
        stats.put("weeklyBreakdown", weeklyBreakdown); // frontend maps this field
        stats.put("month", today.getMonth().toString());

        return stats;
    }
}