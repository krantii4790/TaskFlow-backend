package com.planner.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planner.model.Todo;
import com.planner.model.User;
import com.planner.model.enums.TodoStatus;
import com.planner.repository.TodoRepository;
import com.planner.repository.UserRepository;

@Service
public class TodoService {
    
    @Autowired
    private TodoRepository todoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Todo> getTodosByDate(Long userId, LocalDate date) {
        return todoRepository.findByUserIdAndDate(userId, date);
    }
    
    public List<Todo> getTodosByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return todoRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }
    
    public Todo createTodo(Long userId, LocalDate date, String title, String description) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Todo todo = new Todo();
        todo.setUser(user);
        todo.setDate(date);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setStatus(TodoStatus.PENDING);
        
        return todoRepository.save(todo);
    }
    
    public Todo updateTodo(Long id, TodoStatus status) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        todo.setStatus(status);
        todo.setUpdatedAt(LocalDateTime.now());
        
        return todoRepository.save(todo);
    }
    
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
    
    public long getCompletedCount(Long userId, LocalDate date) {
        return todoRepository.countByUserIdAndDateAndStatus(userId, date, TodoStatus.DONE);
    }
    
    public long getTotalCount(Long userId, LocalDate date) {
        return todoRepository.findByUserIdAndDate(userId, date).size();
    }
}