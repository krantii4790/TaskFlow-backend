package com.planner.dto;

import java.time.LocalDate;

import com.planner.model.enums.TodoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private LocalDate date;
}