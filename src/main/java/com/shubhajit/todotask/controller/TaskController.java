package com.shubhajit.todotask.controller;

import com.shubhajit.todotask.model.TaskDTO;
import com.shubhajit.todotask.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing to-do tasks.
 * Provides CRUD endpoints for Task resources.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Slf4j
@Validated
public class TaskController {
    private final TaskService taskService;

    /**
     * Get all tasks.
     *
     * @return list of all TaskDTOs
     */
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        log.info("Fetching all tasks");
        return taskService.getAllTasks();
    }

    /**
     * Get a task by its ID.
     *
     * @param id the task ID (must be >= 1)
     * @return the TaskDTO if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable @Min(1) Long id) {
        log.info("Fetching task with id: {}", id);
        TaskDTO dto = taskService.getTaskById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Create a new task.
     *
     * @param taskDTO the task data
     * @return the created TaskDTO
     */
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        log.info("Creating new task: {}", taskDTO.getTitle());
        TaskDTO created = taskService.saveTask(taskDTO);
        URI location = URI.create(String.format("/api/tasks/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Update an existing task.
     *
     * @param id      the task ID
     * @param taskDTO the new task data
     * @return the updated TaskDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        log.info("Updating task with id: {}", id);
        taskDTO.setId(id);
        TaskDTO updated = taskService.saveTask(taskDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a task by its ID.
     *
     * @param id the task ID
     * @return response entity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with id: {}", id);
        taskService.deleteTask(id);
        log.info("Task deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
