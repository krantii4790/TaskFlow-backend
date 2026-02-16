package com.planner.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planner.dto.TodoDTO;
import com.planner.dto.TodoRequest;
import com.planner.model.Todo;
import com.planner.model.enums.TodoStatus;
import com.planner.service.ProgressService;
import com.planner.service.TodoService;

@RestController
@RequestMapping("/api/todos")
@PreAuthorize("isAuthenticated()")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private ProgressService progressService;

    // ---------------- GET TODOS BY DATE ----------------
    @GetMapping
    public ResponseEntity<List<TodoDTO>> getTodos(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Todo> todos = todoService.getTodosByDate(userId, date);

        return ResponseEntity.ok(
                todos.stream().map(this::convertToDTO).collect(Collectors.toList())
        );
    }

    // ---------------- GET TODOS BY RANGE ----------------
    @GetMapping("/range")
    public ResponseEntity<List<TodoDTO>> getTodosByRange(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Todo> todos = todoService.getTodosByDateRange(userId, startDate, endDate);

        return ResponseEntity.ok(
                todos.stream().map(this::convertToDTO).collect(Collectors.toList())
        );
    }

    // ---------------- CREATE TODO (JSON BODY) ----------------
    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(@RequestBody TodoRequest request) {

        Todo todo = todoService.createTodo(
                request.getUserId(),
                request.getDate(),
                request.getTitle(),
                request.getDescription()
        );

        progressService.calculateProgressForDay(
                request.getUserId(),
                request.getDate()
        );

        return new ResponseEntity<>(convertToDTO(todo), HttpStatus.CREATED);
    }

    // ---------------- UPDATE TODO ----------------
    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(
            @PathVariable Long id,
            @RequestParam TodoStatus status,
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Todo todo = todoService.updateTodo(id, status);
        progressService.calculateProgressForDay(userId, date);

        return ResponseEntity.ok(convertToDTO(todo));
    }

    // ---------------- DELETE TODO ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        todoService.deleteTodo(id);
        progressService.calculateProgressForDay(userId, date);

        return ResponseEntity.noContent().build();
    }

    private TodoDTO convertToDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setStatus(todo.getStatus());
        dto.setDate(todo.getDate());
        return dto;
    }
}
