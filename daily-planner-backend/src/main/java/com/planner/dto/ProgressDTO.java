package com.planner.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDTO {
    private LocalDate date;
    private Integer totalTasks;
    private Integer completedTasks;
    private Double completionPercentage;
}