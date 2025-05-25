package com.shubhajit.todotask.controller;

import com.shubhajit.todotask.model.Task;
import com.shubhajit.todotask.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        log.info("Fetching all tasks");
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        log.info("Fetching task with id: {}", id);
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Task not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        log.info("Creating new task: {}", task.getTitle());
        return taskService.saveTask(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        log.info("Updating task with id: {}", id);
        return taskService.getTaskById(id)
                .map(existingTask -> {
                    existingTask.setTitle(task.getTitle());
                    existingTask.setDescription(task.getDescription());
                    existingTask.setCompleted(task.isCompleted());
                    log.debug("Updated task details: {}", existingTask);
                    return ResponseEntity.ok(taskService.saveTask(existingTask));
                })
                .orElseGet(() -> {
                    log.warn("Task not found for update with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with id: {}", id);
        if (taskService.getTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            log.info("Task deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Task not found for deletion with id: {}", id);
        return ResponseEntity.notFound().build();
    }
}
