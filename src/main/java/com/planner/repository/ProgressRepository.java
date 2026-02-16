package com.planner.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planner.model.Progress;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByUserIdAndDate(Long userId, LocalDate date);
    List<Progress> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Progress> findByUserId(Long userId);
}
