package com.planner.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planner.model.Todo;
import com.planner.model.User;
import com.planner.model.enums.TodoStatus;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserIdAndDate(Long userId, LocalDate date);
    List<Todo> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    long countByUserIdAndDateAndStatus(Long userId, LocalDate date, TodoStatus status);
    List<Todo> findByUserId(Long userId);
}