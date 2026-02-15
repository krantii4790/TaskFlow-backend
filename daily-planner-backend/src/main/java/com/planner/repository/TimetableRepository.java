package com.planner.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planner.model.Timetable;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByUserIdAndDate(Long userId, LocalDate date);
    List<Timetable> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Timetable> findByUserId(Long userId);
    void deleteByUserIdAndDate(Long userId, LocalDate date);
}