package com.planner.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequest {
    private String title;
    private String description;
    private LocalDate date;
    private Long userId;
}

