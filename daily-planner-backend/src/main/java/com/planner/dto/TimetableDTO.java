package com.planner.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimetableDTO {
    private Long id;
    private String activity;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}